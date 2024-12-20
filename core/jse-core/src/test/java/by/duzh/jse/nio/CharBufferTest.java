package by.duzh.jse.nio;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

public class CharBufferTest {
    private static final String TEXT = "1234567890"; // length = 10
    private static final char[] CHARS = TEXT.toCharArray();

    private static final int CAPACITY = 16;
    private static final int LIMIT = CAPACITY / 4 * 3; // 12
    private CharBuffer buffer;

    @Before
    public void init() {
        buffer = CharBuffer.allocate(CAPACITY); // 16 elements
        buffer.limit(LIMIT); // 12 elements
    }

    @Test
    public void testPut() {
        buffer.put((char) 1);
        buffer.put((char) 2);
        Assert.assertEquals(2, buffer.position());

        buffer.put(5, (char) 5);
        Assert.assertEquals(2, buffer.position()); // NOTE: position not changed

        buffer.clear();
        buffer.put(TEXT.toCharArray(), 2, 3);
        Assert.assertEquals(3, buffer.position());
        buffer.flip();
        CharBuffer buffer1 = CharBuffer.allocate(5);
        buffer1.put(buffer);
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutException() {
        buffer.put(CHARS).put(CHARS);  // 20 elements are being added
    }

    @Test
    public void testGet() {
        buffer.put(CHARS); // 10 elements added

        // One char
        buffer.rewind();
        char b = buffer.get();
        Assert.assertEquals(CHARS[0], b);
        b = buffer.get(3);
        Assert.assertEquals(CHARS[3], b);
        Assert.assertEquals(1, buffer.position());

        // chars
        char[] dest = new char[5];
        buffer.get(dest);
        Assert.assertEquals(6, buffer.position());
        Assert.assertEquals("23456", new String(dest));

        // sub-chars
        Arrays.fill(dest, CHARS[0]);
        buffer.rewind();
        buffer.position(5);
        buffer.get(dest, 1, 3);
        Assert.assertEquals(8, buffer.position());
        Assert.assertEquals("16781", new String(dest));
    }

    @Test(expected = BufferUnderflowException.class)
    public void testGetException() {
        buffer.put(CHARS);
        buffer.rewind();

        char[] dest = new char[100];
        buffer.get(dest);
    }

    @Test
    public void testJDK11Mismatch() {
        buffer = CharBuffer.wrap(CHARS);
        Assert.assertEquals(-1, buffer.mismatch(CharBuffer.wrap(CHARS)));

        buffer = CharBuffer.wrap(CHARS, 1, CHARS.length - 1);
        Assert.assertEquals(0, buffer.mismatch(CharBuffer.wrap(CHARS)));

        buffer = CharBuffer.wrap(CHARS, 0, CHARS.length / 2);
        Assert.assertEquals(CHARS.length / 2, buffer.mismatch(CharBuffer.wrap(CHARS)));
    }
}
