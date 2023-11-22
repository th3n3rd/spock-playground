package com.example.spockplayground

import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter
import spock.lang.Specification

class ModularityTests extends Specification {

    def modules = ApplicationModules.of(DemoApplication)

    def "show basic information for each module"() {
        expect:
        modules.forEach { println it }
    }

    def "verify modularity"() {
        expect:
        modules.verify()
    }

    def "generate documentation"() {
        expect:
        new Documenter(modules).writeDocumentation()
    }
}
