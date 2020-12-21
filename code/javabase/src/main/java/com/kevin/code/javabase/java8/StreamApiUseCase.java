package com.kevin.code.javabase.java8;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * java8 stream API 使用示例
 *
 * @author kevin
 */
public class StreamApiUseCase {

    @Test
    public void intStream() {
        IntStream.range(1, 4)
                .forEach(System.out::println); // 相当于 for (int i = 1; i < 4; i++) {}
    }

    @Test
    public void mapToInt() {
        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1)) // 对每个字符串元素从下标1位置开始截取
                .mapToInt(Integer::parseInt) // 转成 int 基础类型类型流
                .max() // 取最大值
                .ifPresent(System.out::println);  // 不为空则输出
    }

    @Test
    public void mapToObj() {
        IntStream.range(1, 4)
                .mapToObj(i -> "a" + i) // for 循环 1->4, 拼接前缀 a
                .forEach(System.out::println); // for 循环打印
    }

    @Test
    public void retardance() {
        // 演示了流的中间操作的延迟性,流的延迟性操作是为了减少操作的次数,来提升性能
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                });// 无任何内容打印
    }

    /**
     * 对流的中间操作需要采用和时间的顺序,这样可以有效提升流操作的效率,如下对于map()和filter()
     * 操作的演示
     */
    @Test
    public void sequenceOfStream() {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a"); // 过滤出以 a 为前缀的元素
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase(); // 转大写
                })
                .forEach(s -> System.out.println("forEach: " + s)); // for 循环输出

        // filter:  d2
        // filter:  a2
        // map:     a2
        // forEach: A2
        // filter:  b1
        // filter:  b3
        // filter:  c

        // map仅仅只需调用一次，性能得到了提升,如果filter()和map()的顺序颠倒则map()操作执行次数与filter()相同
    }

    /**
     * Java8 Stream 流是不能被复用的，一旦调用任何终端操作，流就会关闭：
     * 但是 可以通过 Supplier 来包装一下流，通过 get() 方法来构建一个新的 Stream 流
     */
    @Test
    public void reuseOfStream() {
        Stream<String> stream =
                Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        stream.anyMatch(s -> true);    // ok
        //stream.noneMatch(s -> true);   // IllegalStateException

        // 构建新的流
        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        System.out.println(streamSupplier.get().anyMatch(s -> true));
        ;   // ok
        System.out.println(streamSupplier.get().noneMatch(s -> true));
        ;  // ok
    }

    List<Person> persons =
            Arrays.asList(
                    new Person("Max", 18),
                    new Person("Peter", 23),
                    new Person("Pamela", 23),
                    new Person("David", 12));

    @Test
    public void collect() {
        // 构建一个 Person 集合

        Double avgAge = persons.stream().collect(Collectors.averagingInt(Person::getAge));
        // 获取Integer类型数据的内置统计对象
        IntSummaryStatistics statistics = persons.stream().collect(Collectors.summarizingInt(Person::getAge));

        //System.out.println(avgAge);
        //System.out.println(statistics);
        Stream<BigDecimal> stream = Stream.of(new BigDecimal("1.22"), new BigDecimal("2.11"), new BigDecimal("6"));
        Double collect = stream.collect(Collectors.averagingDouble(BigDecimal::doubleValue));
        System.out.println(collect);

    }

    /**
     * 将流转换为Map
     */
    @Test
    public void toMap() {
        Map<Integer, String> map = persons
                .stream()
                .collect(Collectors.toMap(
                        p -> p.getAge(),
                        p -> p.getName(),
                        (name1, name2) -> name1 + ";" + name2)); // 对于同样 key 的，将值拼接

        System.out.println(map);
        // {18=Max, 23=Peter;Pamela, 12=David}
    }

    /**
     * 最开始供应器使用分隔符构造了一个 StringJoiner。
     * <p>
     * 累加器用于将每个人的人名转大写，然后加到 StringJoiner 中。
     * <p>
     * 组合器将两个 StringJoiner 合并为一个。
     * <p>
     * 最终，终结器从 StringJoiner 构造出预期的字符串。
     */
    @Test
    public void definedCollector() {
        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),      // supplier 供应器
                        (j, p) -> j.add(p.getName().toUpperCase()),  // accumulator 累加器
                        (j1, j2) -> j1.merge(j2),               // combiner 组合器
                        StringJoiner::toString);                // finisher 终止器

        String names = persons
                .stream()
                .collect(personNameCollector); // 传入自定义的收集器

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    /**
     * 使用flatMap 将流扁平化处理
     */
    @Test
    public void flatMap() {
        List<Foo> foos = new ArrayList<>();

        // 创建 foos 集合
        IntStream
                .range(1, 4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));

        // 创建 bars 集合
        foos.forEach(f ->
                IntStream
                        .range(1, 4)
                        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));
        foos.stream()
                .flatMap(f -> f.bars.stream())
        //.forEach(b -> System.out.println(b.name))
        ;

        // Bar1 <- Foo1
        // Bar2 <- Foo1
        // Bar3 <- Foo1
        // Bar1 <- Foo2
        // Bar2 <- Foo2
        // Bar3 <- Foo2
        // Bar1 <- Foo3
        // Bar2 <- Foo3
        // Bar3 <- Foo3

        // 更简单的写法 TODO
        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
    }

    /**
     * peek 操作 一般用于不想改变流中元素本身的类型或者只想操作元素的内部状态时
     */
    @Test
    public void peek() {
        Stream<String> stream = Stream.of("hello", "felord.cn");
        List<String> strs = stream.peek(System.out::println).collect(Collectors.toList());
    }

    @Test
    public void optionalFlatMap() {
        Optional.of(new Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);

        // 等价于:
        // if (outer != null && outer.nested != null && outer.nested.inner != null) {
        //    System.out.println(outer.nested.inner.foo);
        //}
    }

    /**
     * 规约操作可以将流的所有元素组合成一个结果
     */
    @Test
    public void reduce() {
        persons.stream()
                .reduce((p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2)
                .ifPresent(System.out::println);    // Pamela

        Person result = persons
                .stream()
                .reduce(new Person("", 0), (p1, p2) -> {
                    int age = p1.getAge();
                    age += p1.getAge();
                    String name = p1.getName();
                    name += p2.getName();
                    return p1;
                });

        //System.out.format("name=%s; age=%s", result.getName(), result.getAge());
// name=MaxPeterPamelaDavid; age=76

        // 第三种:
        Integer ageSum = persons
                .stream()
                .reduce(0, (sum, p) -> sum += p.getAge(), (sum1, sum2) -> sum1 + sum2);

        System.out.println(ageSum);  // 76


        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());    // 3

    }

    /**
     * 并行流底层使用的ForkJoinPool, 它由ForkJoinPool.commonPool()方法提供
     */
    @Test
    public void parallelStream() {
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }

    @Test
    public void testFlatMap() {
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        List<Integer> flatList = inputStream
                .flatMap(cList -> cList.stream()) // 将流中的集合类型子元素全部放入到一个流中,将最底层元素抽出来放到一起
                .collect(Collectors.toList());

        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
    }

    @Test
    public void testLimitAndSkip() {
        List<Person> persons = new ArrayList();
        for (int i = 1; i <= 10; i++) {
            Person person = new Person("name" + i, i);
            persons.add(person);
        }
        List<Person> personList2 = persons.stream()
                .sorted(Comparator.comparing(Person::getName))
                .limit(8) // 只返回前8个
                .skip(3) // 跳过前3个
                .collect(Collectors.toList());

        System.out.println(personList2);
    }

    @Test
    public void testStreamGenerate() {
        Random seed = new Random();
        Supplier<Integer> random = seed::nextInt;
        Stream.generate(random).limit(10).forEach(System.out::println);
        //Another way
        IntStream.generate(() -> (int) (System.nanoTime() % 100)).
                limit(10).forEach(System.out::println);
    }

    @Test
    public void testDefinedSupplier() {
        Stream.generate(new PersonSupplier()).
                limit(10).
                forEach(p -> System.out.println(p.getName() + ", " + p.getAge()));
    }

    /**
     * 自定义Supplier
     */
    private class PersonSupplier implements Supplier<Person> {
        private int index = 0;
        private Random random = new Random();

        @Override
        public Person get() {
            return new Person("StormTestUser" + index++, random.nextInt(100));
        }
    }

    @Test
    public void testBigDecimal() {
        List<BigDecimal> bigDecimals = Arrays.asList(new BigDecimal("1.19"), new BigDecimal("2.1"));

        BigDecimal sum = bigDecimals.stream()
                //.map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long count = bigDecimals.stream().filter(Objects::nonNull).count();

        BigDecimal divide = sum.divide(new BigDecimal(count), BigDecimal.ROUND_HALF_UP);
        System.out.println(divide);
    }

}

class Foo {
    String name;
    List<Bar> bars = new ArrayList<>();

    Foo(String name) {
        this.name = name;
    }
}

class Bar {
    String name;

    Bar(String name) {
        this.name = name;
    }
}

class Outer {
    Nested nested;
}

class Nested {
    Inner inner;
}

class Inner {
    String foo;
}

