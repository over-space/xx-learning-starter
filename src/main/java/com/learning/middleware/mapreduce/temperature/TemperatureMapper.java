package com.learning.middleware.mapreduce.temperature;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperatureMapper extends Mapper<Object, Text, TemperatureMapKey, IntWritable> {

    TemperatureMapKey outKey = new TemperatureMapKey();
    IntWritable outValue = new IntWritable();

    private Map<String, String> cityMap = new HashMap<>();

    @Override
    protected void setup(Mapper<Object, Text, TemperatureMapKey, IntWritable>.Context context) throws IOException, InterruptedException {
        URI[] cacheFiles = context.getCacheFiles();

        for (URI cacheFile : cacheFiles) {
            Path path = new Path(cacheFile.getPath());
            BufferedReader reader = new BufferedReader(new FileReader(new File(path.getName())));

            String line = reader.readLine();

            while (line != null) {
                String[] split = line.split(":");
                cityMap.put(split[0], split[1]);
                line = reader.readLine();
            }
        }
    }

    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, TemperatureMapKey, IntWritable>.Context context) throws IOException, InterruptedException {
        // 2021-09-30 11:31:10,S10001,23

        String[] split = StringUtils.split(value.toString(), ",");
        LocalDateTime localDateTime = LocalDateTime.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String cityNo = split[1];
        int temp = Integer.parseInt(split[2]);

        outKey.setYear(localDateTime.getYear());
        outKey.setMonth(localDateTime.getMonth().getValue());
        outKey.setDay(localDateTime.getDayOfMonth());
        outKey.setTemperature(temp);
        outKey.setCity(cityMap.getOrDefault(cityNo, "-"));

        outValue.set(temp);
        context.write(outKey, outValue);
    }
}
