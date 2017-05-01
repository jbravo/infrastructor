package io.infrastructor.core.tasks


public class FetchActionBuilder {
    
    def static execute(closure) {
        closure.delegate = new FetchActionBuilder()
        closure()
    }

    def fetch(Map params) {
        fetch(params, {})
    }
    
    def fetch(Closure closure) {
        fetch([:], closure)
    }
    
    def fetch(Map params, Closure closure) {
        def action = new FetchAction(params)
        action.with(closure)
        return action
    }
}

