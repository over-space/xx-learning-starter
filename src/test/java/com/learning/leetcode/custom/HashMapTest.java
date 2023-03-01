package com.learning.custom;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lifang
 * @since 2022/1/6
 */
public class HashMapTest extends BaseTest {

    @Test
    void testHashMapComputeIfAbsent() {
        Map<String, List<Integer>> map = new HashMap<>();

        // key对应值不存在，则执行lambda，并且将返回结果put到map中。
        List<Integer> list = map.computeIfAbsent("a", (key) -> new ArrayList<>());

        logger.info("list : {}", list);

        list.add(1);
        list.add(2);

        list = map.get("a");
        logger.info("list : {}", list);
    }

    @Test
    void testHashMapComputeIfPresent() {
        Map<String, List<Integer>> map = new HashMap<>();

        // key对应值不存在，则执行Function，并且将返回结果put到map中。
        List<Integer> list = map.computeIfAbsent("a", (key) -> new ArrayList<>());

        list.add(1);
        list.add(2);

        logger.info("list : {}", list);

        // key对应的value存在，则将lambda返回的值替换旧值。
        list = map.computeIfPresent("a", (key, value) -> new ArrayList<>());

        logger.info("list : {}", list);

        list.add(1);
        list.add(2);

        list = map.get("a");
        logger.info("list : {}", list);
        logger.info("map : {}", map);

        // 如果lambda返回结果为null，则将在map中移除key.
        list = map.computeIfPresent("a", (key, value) -> null);

        logger.info("list : {}", list);
        logger.info("map : {}", map);

    }

    @Test
    void testTreeMap() {
        TreeMap<String, String> treeMap = new TreeMap<>();

        treeMap.put("1", "1");
        treeMap.put("2", "2");
        treeMap.put("3", "3");
        treeMap.put("5", "5");
        treeMap.put("4", "4");
        logger.info("treeMap : {}", treeMap);

        // 返回key大于等于3的所有key-value集合。
        SortedMap<String, String> sortedMap = treeMap.tailMap("3");
        logger.info("sortedMap : {}", sortedMap);
        logger.info("sortedMap.firstKey : {}", sortedMap.firstKey());

        Map.Entry<String, String> floorEntry = treeMap.floorEntry("7");
        logger.info("floorEntry : {}", floorEntry);

        String floorKey = treeMap.floorKey("7");
        logger.info("floorKey : {}", floorKey);
    }

    @Test
    void test() {
        test1();
        test2();
    }

    @Test
    void test1() {
        long start = System.currentTimeMillis();
        Map<Integer, List<Integer>> map = new HashMap<>();

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        for (int i = 0; i < 10000000; i++) {

            int key = threadLocalRandom.nextInt(50);

            List<Integer> list = map.getOrDefault(key, new ArrayList<>());

            list.add(threadLocalRandom.nextInt());

            map.put(key, list);

            list.add(threadLocalRandom.nextInt());

            map.put(key, list);
        }
        logger.info("timeout: {}, size: {}", (System.currentTimeMillis() - start) / 1000.0F, map.size());
        map = null;
    }

    @Test
    void test2() {
        long start = System.currentTimeMillis();
        Map<Integer, List<Integer>> map = new HashMap<>();

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        for (int i = 0; i < 10000000; i++) {

            int key = threadLocalRandom.nextInt(50);

            List<Integer> list = map.computeIfAbsent(key, k -> new ArrayList<>());

            list.add(threadLocalRandom.nextInt());

            list.add(threadLocalRandom.nextInt());
        }
        logger.info("timeout: {}, size: {}", (System.currentTimeMillis() - start) / 1000.0F, map.size());
        map = null;
    }
}
