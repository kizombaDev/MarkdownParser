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

package org.kizombadev.markdownparser.exceptions;

public class UnknownSyntaxTypeException extends MarkdownParserException {
    public UnknownSyntaxTypeException() {
        //nothing to do
    }

    public UnknownSyntaxTypeException(String message) {
        super(message);
    }

    public UnknownSyntaxTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownSyntaxTypeException(Throwable cause) {
        super(cause);
    }

    public UnknownSyntaxTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
