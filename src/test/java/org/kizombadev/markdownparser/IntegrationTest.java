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
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.contentOf;

public class IntegrationTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();


    @Test
    public void test() throws URISyntaxException {
        File markdownFile = new File(IntegrationTest.class.getResource("/org/kizombadev/markdownparser/integrationtest/basic.md").toURI());
        File expectedHtmlFile = new File(IntegrationTest.class.getResource("/org/kizombadev/markdownparser/integrationtest/basic.html").toURI());

        File generatedHtmlFile = Paths.get(folder.getRoot().getPath(), "output.html").toFile();

        assertThat(generatedHtmlFile).doesNotExist();
        assertThat(markdownFile).exists();
        assertThat(expectedHtmlFile).exists();

        String[] args = {"-i", markdownFile.getPath(), "-o", generatedHtmlFile.getPath()};
        Program.main(args);

        assertThat(generatedHtmlFile).exists();
        assertThat(contentOf(generatedHtmlFile)).isEqualTo(contentOf(expectedHtmlFile));
    }
}
