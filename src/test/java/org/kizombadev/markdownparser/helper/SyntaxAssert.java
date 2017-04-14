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

package org.kizombadev.markdownparser.helper;

import org.assertj.core.api.AbstractAssert;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;

import java.util.Objects;

public class SyntaxAssert extends AbstractAssert<SyntaxAssert, Syntax> {

    private SyntaxAssert(Syntax actual) {
        super(actual, SyntaxAssert.class);
    }

    public static SyntaxAssert assertThat(Syntax actual) {
        return new SyntaxAssert(actual);
    }

    public SyntaxAssert isTextElementWith(String content) {
        isNotNull();

        assertThat(actual).isSyntaxTypeOf(SyntaxType.TEXT);

        if (!Objects.equals(actual.getContent(), content)) {
            failWithMessage("Expected content to be <%s> but was <%s>", content, actual.getContent());
        }

        return this;
    }

    public SyntaxAssert isSyntaxTypeOf(SyntaxType type) {
        isNotNull();

        if (actual.getType() != type) {
            failWithMessage("Expected syntax type to be <%s> but was <%s>", type, actual.getType());
        }

        return this;
    }
}