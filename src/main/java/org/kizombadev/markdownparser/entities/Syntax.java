package org.kizombadev.markdownparser.entities;


import com.google.common.collect.ImmutableList;
import org.kizombadev.markdownparser.entities.interfaces.ImmutableSyntax;
import org.kizombadev.markdownparser.entities.interfaces.MutableSyntax;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Syntax implements MutableSyntax {
    private List<ImmutableSyntax> children = new ArrayList<>();

    public ImmutableList<ImmutableSyntax> getChildren() {
        return ImmutableList.copyOf(children);
    }

    public void addChild(ImmutableSyntax syntax) {
        checkNotNull(syntax);
        children.add(syntax);
    }

    public <T extends ImmutableSyntax> T convertTo(Class<T> type) {
        return (T) this;
    }
}
