package com.learning.middleware.mapreduce.temperature;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperatureSortComparator extends WritableComparator {

    public TemperatureSortComparator() {
        super(TemperatureMapKey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        // 按年，月，温度排序，并且温度倒叙

        TemperatureMapKey k1 = ((TemperatureMapKey) a);
        TemperatureMapKey k2 = ((TemperatureMapKey) b);

        int c1 = Integer.compare(k1.getYear(), k2.getYear());
        if (c1 == 0) {
            int c2 = Integer.compare(k1.getMonth(), k2.getMonth());
            if (c2 == 0) {
                return -Integer.compare(k1.getTemperature(), k2.getTemperature());
            }
            return c2;
        }
        return c1;
    }
}
