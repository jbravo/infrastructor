package io.infrastructor.core.inventory

import groovy.transform.ToString

import static io.infrastructor.core.processing.ProvisioningContext.provision
import static io.infrastructor.core.validation.ValidationHelper.validate

@ToString(includePackage = false, includeNames = true, ignoreNulls = true)
public class Inventory {
    
    def nodes = []
    
    public void leftShift(Node node) {
        nodes << validate(node)
    }
    
    def provision(Closure closure) {
        provision(nodes, closure)
        this
    }
}

