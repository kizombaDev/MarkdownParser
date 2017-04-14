package org.kizombadev.markdownparser.entities.interfaces;

public interface MutableSyntax extends ImmutableSyntax {
    void addChild(ImmutableSyntax syntax);
}
