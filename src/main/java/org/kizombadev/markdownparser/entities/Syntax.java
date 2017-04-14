package org.kizombadev.markdownparser.entities;


import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Syntax {
    private List<Syntax> children = new ArrayList<>();

    public ImmutableList<Syntax> getChildren() {
        return ImmutableList.copyOf(children);
    }

    public void addChild(Syntax syntax) {
        checkNotNull(syntax);
        children.add(syntax);
    }
}
