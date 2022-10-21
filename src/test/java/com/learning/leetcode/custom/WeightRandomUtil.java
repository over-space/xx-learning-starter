package com.learning.custom;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author lifang
 * @since 2021/12/30
 */
public class WeightRandomUtil {

    private TreeMap<Double, String> weightMap = new TreeMap<>();

    public WeightRandomUtil(List<Pair<String, Integer>> list) {
        for (Pair<String, Integer> pair : list) {

            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey();

            //权重累加
            this.weightMap.put(pair.getValue() + lastWeight, pair.getKey());
        }
    }

    public String random() {
        double randomWeight = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, String> tailMap = this.weightMap.tailMap(randomWeight, true);
        return this.weightMap.get(tailMap.firstKey());
    }


    public static void main(String[] args) {
        List<Pair<String,Integer>> list = new ArrayList<>();
        list.add(Pair.of("A",93));
        list.add(Pair.of("B",3));
        list.add(Pair.of("C",1));
        list.add(Pair.of("D",1));
        list.add(Pair.of("E",1));

        WeightRandomUtil weightRandom = new WeightRandomUtil(list);

        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < 100; i++) {

            String random = (String)weightRandom.random();


            Integer val = result.getOrDefault(random, 0);

            result.put(random, val + 1);
        }
        System.out.println(result);
        result.forEach((key, value) -> {
            System.out.println("key: " + key + ", scale: " + (value / 500.0f));
        });
    }
}
