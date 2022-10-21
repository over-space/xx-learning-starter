package com.learning.cache.lru;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.*;

/**
 * @author lifang
 * @since 2021/12/9
 */
public class LRUCacheTest extends BaseTest {

    @Test
    public void testLRU(){

        LRUCache<Integer, Integer> lruCache = new LRUCache<>(10);

        for (int i = 0; i < 10; i++) {
            lruCache.put(i, i);
        }
        print(lruCache);

        Integer v5 = lruCache.get(5);
        logger.info("v5: {}", v5);
        print(lruCache);

        Integer v6 = lruCache.get(6);
        logger.info("v6: {}", v6);
        print(lruCache);

        lruCache.put(20, 20);
        print(lruCache);

        lruCache.get(1);
        print(lruCache);

        lruCache.get(20);
        print(lruCache);

        lruCache.put(2, 2);
        print(lruCache);

    }

    @Test
    public void testMap(){
        Map<String, List<String>> map = new HashMap<>();

        List<String> list = map.computeIfAbsent("k1", key -> new ArrayList<>());
        list.add("a");
        list.add("b");
        list.add("c");
        logger.info("map : {}", map);

        map.computeIfAbsent("k1", key -> new ArrayList<>());

        map.put("k2", new ArrayList<>());
        logger.info("map : {}", map);

        map.put("k3", null);

        map.computeIfPresent("k3", (key, value) -> {
            System.out.println(key);
            System.out.println(value);

            value.remove("c");
            value.add("d");
            return value;
        });
        logger.info("map : {}", map);
    }

    private void print(LRUCache<Integer, Integer> lruCache){

        LRUCache.Node<Integer, Integer> head = lruCache.getDoublyLinkedList().getHead();

        StringBuffer stringBuffer = new StringBuffer();
        while (head != null){
            head = head.getNext();

            if(head != null) {
                stringBuffer.append(head.getValue()).append(", ");
            }
        }

        logger.info("linked list : {}", stringBuffer);
    }

}
