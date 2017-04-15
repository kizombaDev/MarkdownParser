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

import org.apache.commons.cli.*;
import org.kizombadev.markdownparser.exceptions.MarkdownParserException;

import java.io.*;

public class Application {
    private static final String OUTPUT_OPTION = "output";
    private static final String INPUT_OPTION = "input";
    private static final String HELP_OPTION = "help";
    private final Options options = new Options();
    private CommandLine cmd;

    private Application() {
        initCommandLineParser();
    }

    public static Application create() {
        return new Application();
    }

    public void execute(String[] args) {
        parseArgs(args);
        startOption();
    }

    private void parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new MarkdownParserException("Parsing failed.  Reason: " + e.getMessage(), e);
        }
    }

    private void startOption() {
        if (cmd.hasOption(INPUT_OPTION) && cmd.hasOption(OUTPUT_OPTION)) {
            executeParser(cmd);
        } else {
            printHelp();
        }
    }

    private void executeParser(CommandLine cmd) {

        File outputFile = new File(cmd.getOptionValue(OUTPUT_OPTION));
        createOutputFile(outputFile);
        doExecuteParser(cmd, outputFile);
    }

    private void doExecuteParser(CommandLine cmd, File outputFile) {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            InputStream inputStream = new FileInputStream(cmd.getOptionValue("input"));

            MarkdownParser.create().parse(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            throw new MarkdownParserException("The file was not found", e);
        } catch (IOException e) {
            throw new MarkdownParserException("Unknown error", e);
        }
    }

    private void createOutputFile(File outputFile) {
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            throw new MarkdownParserException("Cannot create the output file", e);
        }
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MarkdownParser", options);
    }

    private void initCommandLineParser() {
        options.addOption(OUTPUT_OPTION.substring(0, 1), OUTPUT_OPTION, true, "the name of the output file (HTML)");
        options.addOption(INPUT_OPTION.substring(0, 1), INPUT_OPTION, true, "the name of the input file (Markdown)");
        options.addOption(HELP_OPTION.substring(0, 1), HELP_OPTION, false, "print this message");
    }


}
