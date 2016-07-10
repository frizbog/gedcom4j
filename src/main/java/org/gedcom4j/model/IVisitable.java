package org.gedcom4j.model;

/**
 * Interface for implementing visitor pattern. Classes that can be visited should implement this pattern, and in the
 * accept method, call the visit() method on the visitor, passing "this" as the argument.
 * 
 * @author frizbog
 */
public interface IVisitable {
    /**
     * Accept a visit from the {@link IVisitor} instance. The proper response is generally to call that object's visit()
     * method, passing "this" as the parameter.
     * 
     * @param v
     *            the class that is visiting this object
     */
    void accept(IVisitor v);
}
