package org.kizombadev.markdownparser.entities.interfaces;


import com.google.common.collect.ImmutableList;

public interface ImmutableSyntax {
    ImmutableList<ImmutableSyntax> getChildren();

    <T extends ImmutableSyntax> T convertTo(Class<T> type);
}
