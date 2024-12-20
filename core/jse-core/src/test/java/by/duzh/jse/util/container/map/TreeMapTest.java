package by.duzh.jse.util.container.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

// TODO: Look into the details of the implementation
// TODO: write tests
public class TreeMapTest {
    private TreeMap<Integer, String> map;

    @Before
    public void init() {
        map = new TreeMap<>();
        map.put(2, "two");
        map.put(1, "one");
        map.put(3, "three");
    }

    @Test
    public void testDefaultComparator() {
        int i = 1;
        for (Map.Entry<Integer, String> e: map.entrySet()) {
            Assert.assertEquals(i++, e.getKey().intValue());
        }
    }

    @Test
    public void testCustomComparator() {
        map = new TreeMap<>((a, b) -> b - a);
        map.put(2, "two");
        map.put(1, "one");
        map.put(3, "three");

        int i = 3;
        for (Map.Entry<Integer, String> e: map.entrySet()) {
            Assert.assertEquals(i--, e.getKey().intValue());
        }
    }

}
