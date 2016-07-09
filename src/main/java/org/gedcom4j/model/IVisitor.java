package org.gedcom4j.model;

/**
 * Interface for implementing the visitor pattern. Classes that want to visit the object model to do various things with
 * it should implement this interface, and override/implement the {@link #visit(IVisitable)} method polymorphically.
 * 
 * @author frizbog
 */
public interface IVisitor {
    /**
     * Visit a visitable class. The visitable class will call this method back.
     * 
     * @param v
     */
    void visit(IVisitable v);
}
