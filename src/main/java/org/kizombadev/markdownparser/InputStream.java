package org.kizombadev.markdownparser;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class InputStream {
    private String content;
    private int index = 0;
    private char current;

    private InputStream() {
    }

    public static InputStream create(String content) {
        checkNotNull(content);

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
        checkState(hasNext());
        return content.charAt(index);
    }

    public boolean hasNext() {
        return index < content.length();
    }
}