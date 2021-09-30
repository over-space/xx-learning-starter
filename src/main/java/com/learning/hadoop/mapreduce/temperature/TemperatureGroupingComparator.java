package com.learning.hadoop.mapreduce.temperature;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperatureGroupingComparator extends WritableComparator {

    public TemperatureGroupingComparator() {
        super(TemperatureMapKey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        // 按年，月排序

        TemperatureMapKey k1 = ((TemperatureMapKey) a);
        TemperatureMapKey k2 = ((TemperatureMapKey) b);

        int c1 = Integer.compare(k1.getYear(), k2.getYear());
        if (c1 == 0) {
            return Integer.compare(k1.getMonth(), k2.getMonth());
        }
        return c1;
    }
}
