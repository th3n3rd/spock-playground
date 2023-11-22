package com.example.spockplayground.gameplay


import spock.lang.Specification

class HintProgressionTests extends Specification {

    def hintProgression = new HintProgression("secret");

    def "each hint will reveal one letter of the secret word, alternating from each side"(int attempts, String expectedHint) {
        when:
        def hint = hintProgression.nextHint(attempts)

        then:
        hint == expectedHint

        where:
        attempts | expectedHint
        0        | "______"
        1        | "s_____"
        2        | "s____t"
        3        | "se___t"
        4        | "se__et"
        5        | "sec_et"
        6        | "secret"
    }
}
