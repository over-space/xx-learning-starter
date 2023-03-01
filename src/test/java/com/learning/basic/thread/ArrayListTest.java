package com.learning.basic.thread;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author 李芳
 * @since 2022/9/30
 */
public class ArrayListTest extends BaseTest {

    @Test
    public void test() {

        List<Integer> list = new ArrayList<>();

        IntStream.range(1, 10000).parallel().forEach(num -> list.add(100));

        boolean empty = list.stream().anyMatch(num -> num == null);

        System.out.println("empty: " + empty);
    }

}
