package by.duzh.jse.util.stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.*;

public class StreamTests {
    private final Integer[] ARRAY_SOURCE = {1, 2, 15, 16, 17, 18, 19, 20, 21, 22, 24, 56, 60, 62, 63};

    private Stream<Integer> stream;

    @Before
    public void init() {
        stream = Arrays.stream(ARRAY_SOURCE);
    }

    @Test
    public void createStream() {
        // From Collection
        Collection<Integer> collection = Arrays.asList(ARRAY_SOURCE);
        stream = collection.stream();

        // Creating a stream from an array - encapsulates the code above
        stream = Stream.of(1, 2, 3);

        // From array
        stream = Arrays.stream(ARRAY_SOURCE);
    }

    @Test
    public void createParallelStream() {
        // From Collection
        stream = Arrays.asList(1, 2, 3).parallelStream();

        // from a sequential stream
        stream = Arrays.stream(ARRAY_SOURCE).parallel();
    }

    @Test
    public void testIntermediateAndTerminalMethods() {
        boolean[] wasCalled = {false};

        // intermediate method (lazy) - > peek is not called, as there is no terminal method
        stream.peek(element -> wasCalled[0] = true);
        Assert.assertFalse(wasCalled[0]);

        // terminal method (eager) -> peek gets called as there is a terminal method
        stream = Stream.of(ARRAY_SOURCE);
        stream.peek(element -> wasCalled[0] = true).count();
        Assert.assertTrue(wasCalled[0]);
    }

    @Test
    public void testPeak() {
        // does not modify the stream
        stream = stream.peek(element -> {
            // do some staff like System.out.println(element);
        });

        stream.count();
    }

    @Test
    public void testCount() {
        Assert.assertEquals(15l, stream.count());
    }

    @Test
    public void testLimit() {
        stream = stream.limit(2);
        Assert.assertEquals(2, stream.count());
    }

    @Test
    public void testJDK9TakeWhile() {
        // for sorted list
        stream = Stream.of(1, 2, 3, 4, 5, 6, 7).takeWhile(i -> i < 4);
        Assert.assertEquals("[1, 2, 3]", stream.collect(Collectors.toList()).toString());

        stream = Stream.of(1, 2).takeWhile(i -> i < 5 );
        Assert.assertEquals("[1, 2]", stream.collect(Collectors.toList()).toString());

        stream = Stream.of(1, 2).takeWhile(i -> i > 5 );
        Assert.assertEquals(0, stream.count());
    }

    @Test
    public void testSkip() {
        stream = stream.skip(13);
        Assert.assertEquals(2, stream.count());
    }

    @Test
    public void testJDK9DropWhile() {
        // for sorted list
        stream = Stream.of(1, 2, 3, 4, 5, 6, 7).dropWhile(i -> i < 5);
        Assert.assertEquals("[5, 6, 7]", stream.collect(Collectors.toList()).toString());

        stream = Stream.of(1, 2).dropWhile(i -> i > 5 );
        Assert.assertEquals("[1, 2]", stream.collect(Collectors.toList()).toString());

        stream = Stream.of(1, 2).dropWhile(i -> i < 5 );
        Assert.assertEquals(0, stream.count());
    }

    @Test
    public void testFindFirst() {
        Optional<Integer> first = stream.findFirst();
        Assert.assertEquals(1, first.get().intValue());

        Integer[] src = {};
        first = Stream.of(src).findFirst();
        Assert.assertFalse(first.isPresent());
    }

    @Test
    public void testFindAny() {
        Optional<Integer> any = stream.findAny();
        Assert.assertTrue(any.isPresent());
    }

    @Test
    public void testConcat() {
        Stream<Integer> stream1 = Stream.of(1, 3);
        Stream<Integer> stream2 = Stream.of(2, 4);

        stream = Stream.concat(stream1, stream2);

        Assert.assertEquals("[1, 3, 2, 4]", Arrays.toString(stream.toArray(Integer[]::new)));
    }

    @Test
    public void testDistinct() {
        stream = Stream.of(1, 3, 1, 4).distinct();
        Assert.assertEquals("[1, 3, 4]", Arrays.toString(stream.toArray(Integer[]::new)));
    }

    @Test
    public void testForEach() {
        int[] count = {0};
        stream.forEach(i -> count[0] += i);

        Assert.assertEquals(416, count[0]);
    }

    @Test
    public void testForEachOrdered() {
        int[] count = {0};
        stream.parallel().forEachOrdered(i -> count[0] += i);

        Assert.assertEquals(416, count[0]);
    }

    @Test
    public void testFilter() {
        Stream<Integer> filtered = stream.filter(value -> value >= 16 && value < 18);
        Assert.assertEquals(2, filtered.count());

        // filter odd
        filtered = stream.filter(value -> value >= 6 && value <= 20)
                .filter(value -> value % 2 == 1);
        Assert.assertEquals(3, filtered.count());
    }

    @Test
    public void testAllMatch() {
        boolean res = stream.allMatch(i -> i > 0);
        Assert.assertTrue(res);

        stream = Stream.of(ARRAY_SOURCE);
        res = stream.allMatch(i -> i < 15);
        Assert.assertFalse(res);
    }

    @Test
    public void testAnyMatch() {
        boolean res = stream.anyMatch(i -> i % 2 == 1);
        Assert.assertTrue(res);

        stream = Stream.of(ARRAY_SOURCE);
        res = stream.anyMatch(i -> i < 0);
        Assert.assertFalse(res);
    }

    @Test
    public void testNoneMatch() {
        boolean res = stream.noneMatch(i -> i < 0);
        Assert.assertTrue(res);
    }

    @Test
    public void testMap() {
        Stream<String> strings = stream.map(i -> String.valueOf(i));
    }

    @Test
    public void testMapToDouble() {
        DoubleStream doubles = stream.mapToDouble(Double::new);
    }

    @Test
    public void testMapToInt() {
        IntStream ints = stream.mapToInt(i -> i * 100);
    }

    @Test
    public void testMapToLong() {
        LongStream longs = stream.mapToLong(Long::new);
    }

    @Test
    public void testJDK9OfNullable() {
        // value == null? Stream.empty(): Stream.of(value)

        // for null value
        stream = Stream.ofNullable(null);
        Assert.assertEquals(0, stream.count());

        // for NOT null value
        stream = Stream.ofNullable(1);
        Assert.assertEquals(1, stream.count());
    }

    @Test
    public void testFlatMap() {
        // Takes the stream of Strings and for each element returns the stream of integers
        Stream<String> stream = Stream.of("55555.333.1", "333.22");

        Stream<Integer> lengths = stream.flatMap(s -> {
            LinkedList<Integer> list = new LinkedList<>();

            StringTokenizer st = new StringTokenizer(s, ".");
            while (st.hasMoreTokens()) {
                list.add(st.nextToken().length());
            }

            return list.stream();
        });

        Assert.assertEquals("[5, 3, 1, 3, 2]", Arrays.toString(lengths.toArray(Integer[]::new)));
    }

    @Test
    public void testFlatMapToInt() {
        // Takes the stream of Strings and for each element returns the stream of integers
        Stream<String> stream = Stream.of("55555.333.1", "333.22");

        IntStream lengths = stream.flatMapToInt(s -> {
            LinkedList<Integer> list = new LinkedList<>();

            StringTokenizer st = new StringTokenizer(s, ".");
            while (st.hasMoreTokens()) {
                list.add(st.nextToken().length());
            }

            return list.stream().mapToInt(Integer::intValue);
        });

        Assert.assertEquals("[5, 3, 1, 3, 2]", Arrays.toString(lengths.toArray()));
    }

    @Test
    public void testMax() {
        Optional<Integer> max = stream.max(Integer::compareTo);
        Assert.assertEquals(63, max.get().intValue());
    }

    @Test
    public void testMin() {
        Optional<Integer> min = stream.min((a, b) -> a - b);
        Assert.assertEquals(1, min.get().intValue());
    }

    @Test
    // Reduction Operations
    public void testReduce() {
        // Variant #1
        Optional<Integer> count = stream.reduce((accumulator, element) -> accumulator + 1);
        Assert.assertEquals(15, count.get().intValue());

        stream = Arrays.stream(new Integer[]{1, 2, 3});
        int multiplication = stream.reduce((accumulator, element) -> accumulator * element).get();
        Assert.assertEquals(6, multiplication);

        Stream<String> stringStream = Stream.of("One", "Two", "Three", "Four");
        String concatenation = stringStream.reduce((accumulator, element) -> accumulator + ", " + element).get();
        Assert.assertEquals("One, Two, Three, Four", concatenation);

        // Variant #2 - with the identity value for the accumulating function
        stream = Arrays.stream(ARRAY_SOURCE);
        //int sum = stream.reduce(0, Integer::sum);
        int sum = stream.reduce(0, (accumulator, element) -> accumulator + element);
        Assert.assertEquals(416, sum);

        stream = Arrays.stream(new Integer[]{1, 2, 3});
        multiplication = stream.reduce(1, (accumulator, element) -> accumulator * element);
        Assert.assertEquals(6, multiplication);
    }

    @Test
    public void testReduceOnParallelStream() {
        // obtain a parallel stream
        stream = stream.parallel();

        // call the reduce with the same combiner and accumulator
        int sum = stream.reduce(0, (accumulator, element) -> accumulator + element,
                (combiner, element) -> combiner + element);
        Assert.assertEquals(416, sum);

        // Use a another combiner
        Stream<Double> doubleStream = Arrays.stream(new Double[]{1.0, 2.0, 3.0, 4.0}).parallel();
        double res = doubleStream.reduce(1.0,
                (accumulator, element) -> accumulator * Math.sqrt(element),
                (combiner, element) -> combiner * element);
        Assert.assertEquals(4.8989, res, 0.001);

        // Unexpected result for a parallel stream when combiner and accumulator is the same
        doubleStream = Arrays.stream(new Double[]{1.0, 2.0, 3.0, 4.0}).parallel();
        res = doubleStream.reduce(1.0, (accumulator, element) -> accumulator * Math.sqrt(element));
        Assert.assertEquals(1.861, res, 0.001);
    }

    @Test
    public void testSorted() {
        Integer[] source = {4, 1, 3, 7};

        // without a comparator
        stream = Stream.of(source).sorted();
        Assert.assertEquals("[1, 3, 4, 7]", Arrays.toString(stream.toArray(Integer[]::new)));

        // with a comparator
        stream = Stream.of(source).sorted((a, b) -> b - a);
        Assert.assertEquals("[7, 4, 3, 1]", Arrays.toString(stream.toArray(Integer[]::new)));
    }

    @Test
    public void testToArray() {
        // Without type
        Object[] objects = stream.toArray();

        // With type
        stream = Arrays.stream(ARRAY_SOURCE);
        Integer[] integers = stream.toArray(size -> new Integer[size]);

        stream = Arrays.stream(ARRAY_SOURCE);
        integers = stream.toArray(Integer[]::new);
    }

    @Test
    // Obtains a collection from a stream
    public void testCollect() {
        // Option #1 - with Collectors
        List<Integer> list = stream.collect(Collectors.toList());
        Assert.assertEquals(15, list.size());

        // Option #2 - with Supplier
        stream = Arrays.stream(ARRAY_SOURCE);
        list = stream.collect(
                () -> new ArrayList<>(),
                (arrayList, element) -> arrayList.add(element),
                (list1, list2) -> list1.addAll(list2));
        Assert.assertEquals(15, list.size());

        stream = Arrays.stream(ARRAY_SOURCE);
        list = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        Assert.assertEquals(15, list.size());
    }

    @Test
    public void testChaining() {
        // Sum all the odd elements * 10
        int sumOdds = stream
                .filter(e -> e % 2 == 1)            // intermediate method (lazy)
                //.peek(System.out::println)
                .map(e -> e * 10)                   // intermediate method (lazy)
                .reduce((a, e) -> a + e).get();     // terminal method (eager)
        Assert.assertEquals(1360, sumOdds);
    }

    @Test
    public void testJDK9Iterate() {
        stream = Stream.iterate(1, i -> i <= 3, i -> i + 1);
        Assert.assertEquals("[1, 2, 3]", stream.collect(Collectors.toList()).toString());
    }
}