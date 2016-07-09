package org.gedcom4j.model;

/**
 * Interface for implementing visitor pattern. Classes that can be visited should implement this pattern, and in the
 * accept method, call the {@link IVisitor#visit(IVisitable)} method, passing "this" as the argument. This is done for
 * most classes in the object model in the {@link AbstractElement} class.
 * 
 * @author frizbog
 */
public interface IVisitable {
    void accept(IVisitor v);
}
