package com.example.spockplayground

import spock.lang.Specification

class BasicSpockTests extends Specification {

    def "a new stack is initialised as empty"() {
        expect:
        new Stack().empty
    }

    def "a stack with one or more items is not empty"() {
        given:
        def one = new Stack()
        def many = new Stack()

        when:
        one.push("a")
        many.push("b")
        many.push("c")

        then:
        !one.empty
        !many.empty
    }

    def "retrieve an item from an empty stack is not allowed"() {
        given:
        def empty = new Stack()

        when:
        empty.pop()

        then:
        thrown(EmptyStackException)
    }

    def "popping items out of a non-empty stack follows a last-in-first-out model"() {
        given:
        def nonEmpty = new Stack()
        nonEmpty.push("first")
        nonEmpty.push("second")
        nonEmpty.push("third")

        expect:
        with(nonEmpty) {
            pop() == "third"
            pop() == "second"
            pop() == "first"
        }
    }
}
