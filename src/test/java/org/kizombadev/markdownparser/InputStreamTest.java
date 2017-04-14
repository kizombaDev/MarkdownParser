package org.kizombadev.markdownparser;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InputStreamTest {

    @Test
    public void testEmptyInput() {
        final InputStream tokenStream = InputStream.create("");

        assertThat(tokenStream.hasNext()).isFalse();

        assertThatThrownBy(tokenStream::next).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(tokenStream::showNext).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testOneCharacterInput() {
        InputStream tokenStream = InputStream.create("a");

        assertThat(tokenStream.hasNext()).isTrue();
        assertThat(tokenStream.showNext()).isEqualTo('a');
        assertThat(tokenStream.next()).isEqualTo('a');

        assertThat(tokenStream.hasNext()).isFalse();

        assertThatThrownBy(tokenStream::showNext).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testLargeInput() {
        InputStream tokenStream = InputStream.create("abc");
        assertThat(tokenStream.next()).isEqualTo('a');
        assertThat(tokenStream.next()).isEqualTo('b');
        assertThat(tokenStream.next()).isEqualTo('c');
    }
}
