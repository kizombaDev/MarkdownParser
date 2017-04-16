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

package org.kizombadev.markdownparser;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;

public class SyntaxRewriter {
    @NotNull
    public static SyntaxRewriter create() {
        return new SyntaxRewriter();
    }

    public Syntax rewrite(Syntax root) {

        rewriteParagraph(root);
        removeMultipleBlanks(root);
        removeBlanksAtTheEnd(root);
        removeParagraphSeparator(root);

        return root;
    }

    private void removeParagraphSeparator(Syntax currentRoot) {
        for (int i = 0; i < currentRoot.getChildrenCount(); i++) {
            if (SyntaxType.PARAGRAPH_SEPARATOR.equals(currentRoot.getChildAt(i).getType())) {
                currentRoot.removeChildAt(i);
            }
        }

        currentRoot.getChildren().stream().forEach(this::removeParagraphSeparator);
    }

    private void removeBlanksAtTheEnd(Syntax currentRoot) {
        if (currentRoot.getChildrenCount() == 0) {
            return;
        }

        Syntax lastChild = currentRoot.getChildAt(currentRoot.getChildrenCount() - 1);
        if (SyntaxType.TEXT.equals(lastChild.getType()) && StringUtils.isBlank(lastChild.getContent())) {
            currentRoot.removeChildAt(currentRoot.getChildrenCount() - 1);
        }

        currentRoot.getChildren().stream().forEach(this::removeBlanksAtTheEnd);
    }

    private void removeMultipleBlanks(Syntax currentRoot) {
        for (int i = 0; i < currentRoot.getChildrenCount() - 1; i++) {
            if (" ".equals(currentRoot.getChildAt(i).getContent()) && " ".equals(currentRoot.getChildAt(i + 1).getContent())) {
                currentRoot.removeChildAt(i + 1);
            }
        }

        currentRoot.getChildren().stream().forEach(this::removeMultipleBlanks);
    }

    private void rewriteParagraph(Syntax root) {
        for (int i = 0; i < root.getChildrenCount() - 1; i++) {
            if (SyntaxType.PARAGRAPH.equals(root.getChildAt(i).getType()) && SyntaxType.PARAGRAPH.equals(root.getChildAt(i + 1).getType())) {

                root.getChildAt(i).addChild(Syntax.createTextSyntax(" "));

                for (Syntax child : root.getChildAt(i + 1).getChildren()) {
                    root.getChildAt(i).addChild(child);
                }

                root.removeChildAt(i + 1);

                rewriteParagraph(root);
            }
        }
    }
}
