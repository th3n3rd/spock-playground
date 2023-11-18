package com.example.spockplayground

import spock.lang.Specification

class InteractionBasedSpockTests extends Specification {

    def publisher = new Publisher()

    def "forward a published message to all subscribers"() {
        given:
        def firstSubscriber = Mock(Subscriber);
        def secondSubscriber = Mock(Subscriber);
        publisher.subscribers << firstSubscriber
        publisher.subscribers << secondSubscriber

        when:
        publisher.send("some-message")

        then:
        1 * firstSubscriber.receive("some-message") >> Result.Ok
        1 * secondSubscriber.receive("some-message") >> Result.Ok
    }

    def "forward published messages messages in the exact order"() {
        given:
        def subscriber = Mock(Subscriber)
        publisher.subscribers << subscriber

        when:
        publisher.send("first")
        publisher.send("second")
        publisher.send("third")

        then:
        with(subscriber) {
            1 * receive("first") >> Result.Ok
            1 * receive("second") >> Result.Ok
            1 * receive("third") >> Result.Ok
        }
    }

    def "retry up to 3 times to forward a message"() {
        given:
        def subscriber = Mock(Subscriber)
        publisher.subscribers << subscriber

        when:
        publisher.send("some-message")

        then:
        3 * subscriber.receive("some-message") >> Result.Failed
    }

    def "stop retrying to forward any message at the first success"() {
        given:
        def subscriber = Mock(Subscriber)
        publisher.subscribers << subscriber

        when:
        publisher.send("some-message")

        then:
        1 * subscriber.receive("some-message") >> Result.Failed
        1 * subscriber.receive("some-message") >> Result.Ok
    }

    static class Publisher {
        def subscribers = [] as List<Subscriber>
        void send(String message) {
            subscribers.each {
                def remainingAttempt = 3
                while (remainingAttempt) {
                    def result = it.receive(message)
                    if (result == Result.Ok) {
                        break
                    }
                    remainingAttempt--
                }
            }
        }
    }

    interface Subscriber {
        Result receive(String message)
    }

    enum Result {
        Ok,
        Failed
    }
}
