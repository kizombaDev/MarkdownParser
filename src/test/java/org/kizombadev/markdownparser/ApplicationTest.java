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

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ApplicationTest {

    private static final String NEW_LINE = System.lineSeparator();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void testWithOutArgs() {
        Application.create().execute(null);

        String expectdConsoleOut = "Ups ... no valid options was found." + NEW_LINE + NEW_LINE +
                "usage: MarkdownParser" + NEW_LINE +
                " -h,--help           print this message" + NEW_LINE +
                " -i,--input <arg>    the name of the input file (Markdown)" + NEW_LINE +
                " -o,--output <arg>   the name of the output file (HTML)" + NEW_LINE;

        assertThat(systemOutRule.getLog()).isEqualTo(expectdConsoleOut);
    }

    @Test
    public void testHelp() {

        Application.create().execute(new String[]{"-h"});

        String expectdConsoleOut = "usage: MarkdownParser" + NEW_LINE +
                " -h,--help           print this message" + NEW_LINE +
                " -i,--input <arg>    the name of the input file (Markdown)" + NEW_LINE +
                " -o,--output <arg>   the name of the output file (HTML)" + NEW_LINE;


        assertThat(systemOutRule.getLog()).isEqualTo(expectdConsoleOut);
    }
}
