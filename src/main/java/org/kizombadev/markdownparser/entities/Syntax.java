/*
 * Marcel Swoboda
 * Copyright (C) 2017
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package org.kizombadev.markdownparser.entities;


import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Syntax {
    private final List<Syntax> children = new ArrayList<>();
    private String content = null;
    private SyntaxType type;

    @NotNull
    public static Syntax createWithChildren(SyntaxType type, Syntax... children) {
        checkNotNull(type);

        Syntax syntax = new Syntax();
        if (children != null) {
            Arrays.stream(children).forEach(syntax::addChild);
        }
        syntax.type = type;
        return syntax;
    }

    @NotNull
    public static Syntax createTextSyntax(String content) {
        checkNotNull(SyntaxType.TEXT);

        Syntax syntax = new Syntax();
        syntax.content = content;
        syntax.type = SyntaxType.TEXT;
        return syntax;
    }

    @NotNull
    public static Syntax create(SyntaxType type) {
        return createWithChildren(type);
    }

    public ImmutableList<Syntax> getChildren() {
        return ImmutableList.copyOf(children);
    }

    public void addChild(Syntax syntax) {
        checkNotNull(syntax);
        children.add(syntax);
    }

    @Nullable
    public String getContent() {
        return content;
    }

    @NotNull
    public SyntaxType getType() {
        return type;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Syntax syntax = (Syntax) o;

        return Objects.equal(children, syntax.children) && Objects.equal(content, syntax.content) && Objects.equal(type, syntax.type);
    }

    @Override
    public int hashCode() {
        int result = children.hashCode();
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }*/
}
