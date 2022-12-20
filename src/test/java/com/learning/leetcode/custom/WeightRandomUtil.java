package com.learning.leetcode.custom;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lifang
 * @since 2021/12/30
 */
public class WeightRandomUtil {

    private TreeMap<Double, String> weightMap = new TreeMap<>();

    public WeightRandomUtil(List<Pair<String, Integer>> list) {
        initWeight(list);
    }

    private void initWeight(List<Pair<String, Integer>> list){
        for (Pair<String, Integer> pair : list) {

            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey();

            //权重累加
            this.weightMap.put(pair.getValue() + lastWeight, pair.getKey());
        }
    }

    public String random() {
        SortedMap<Double, String> tailMap = null;
        do {
            double randomWeight = this.weightMap.lastKey() * ThreadLocalRandom.current().nextDouble();

            tailMap = this.weightMap.tailMap(randomWeight, true);

            if (tailMap != null && !tailMap.isEmpty()) {
                return this.weightMap.get(tailMap.firstKey());
            }
        } while (tailMap == null || tailMap.isEmpty());
        return null;
    }


    public static void main(String[] args) {
        List<Pair<String, Integer>> list = new ArrayList<>();
        list.add(Pair.of("华为", 30));
        list.add(Pair.of("苹果", 40));
        list.add(Pair.of("三星", 5));
        list.add(Pair.of("OPPO", 20));
        list.add(Pair.of("小米", 15));

        WeightRandomUtil weightRandom = new WeightRandomUtil(list);

        for (int j = 0; j < 100; j++) {
            Map<String, Integer> result = new HashMap<>();
            for (int i = 0; i < 100; i++) {

                String random = weightRandom.random();

                Integer val = result.getOrDefault(random, 0);

                result.put(random, val + 1);
            }
            System.out.println(result);
            result.forEach((key, value) -> {
                System.out.println("key: " + key + ", scale: " + value);
            });
            System.out.println("-----------------------------------------------");
        }

    }
}
