package org.kizombadev.markdownparser;


public class InputStream {
    private String content;
    private int index = 0;
    private char current;

    private InputStream() {
    }

    public static InputStream create(String content) {
        if (content == null) {
            throw new IllegalStateException();
        }

        InputStream result = new InputStream();
        result.content = content;
        return result;
    }

    public char next() {
        current = showNext();
        index++;
        return current;
    }

    public char current() {
        return current;
    }

    public char showNext() {
        if (!hasNext()) {
            throw new IllegalStateException();
        }

        return content.charAt(index);
    }

    public boolean hasNext() {
        return index < content.length();
    }
}