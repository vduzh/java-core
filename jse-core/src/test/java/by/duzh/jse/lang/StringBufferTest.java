package by.duzh.jse.lang;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringBufferTest {
    private StringBuffer sb;

    @Before
    public void init() {
        sb = new StringBuffer("test");
    }

    @Test
    public void testLengthCapacity() {
        Assert.assertEquals(4, sb.length());
        Assert.assertEquals(20, sb.capacity());
    }

    @Test
    public void testEnsureCapacity() {
        sb.ensureCapacity(1000);
        Assert.assertEquals(4, sb.length());
        Assert.assertEquals(1000, sb.capacity());
    }

    @Test
    public void testSetLength() {
        sb.setLength(6);
        Assert.assertEquals(6, sb.length());
        // TODO:
        //System.out.println('-' + String.valueOf(sb) + '-');
        //Assert.assertEquals("test  ", sb.toString());
    }

    @Test
    public void testInsert() {
        sb.insert(1, 123);
        Assert.assertEquals("t123est", sb.toString());
    }

    @Test
    public void testReverse() {
        Assert.assertEquals("tset", sb.reverse().toString());
    }

    @Test
    public void testReplace() {
        sb = sb.replace(1, 3, "123456789");
        Assert.assertEquals("t123456789t", sb.toString());
    }

    @Test
    public void testJDK11CompareTo() {
        Assert.assertTrue(new StringBuffer("abc").compareTo(new StringBuffer("abc")) == 0);
        Assert.assertTrue(new StringBuffer("ABC").compareTo(new StringBuffer("abc")) < 0);
        Assert.assertTrue(new StringBuffer("abc").compareTo(new StringBuffer("ABC")) > 0);
    }
}
