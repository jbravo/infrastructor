package io.infrastructor.core.processing.actions

import javax.validation.constraints.NotNull

class ShellAction {
    @NotNull
    def command
    def sudo
    
    def execute(def node) {
        node.execute(command: command, sudo: sudo)
        node.lastResult
    }
}
