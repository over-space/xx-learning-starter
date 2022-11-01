package com.learning.middleware.mapreduce.temperature;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperatureMapKey implements WritableComparable<TemperatureMapKey> {

    private int year;

    private int month;

    private int day;

    private int temperature;

    private String city;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int compareTo(TemperatureMapKey that) {
        // 按年月日排序
        int c1 = Integer.compare(this.year, that.year);
        if(c1 == 0){
            int c2 = Integer.compare(this.month, that.month);
            if(c2 == 0){
                return Integer.compare(this.day, that.day);
            }
            return c2;
        }
        return c1;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(year);
        out.writeInt(month);
        out.writeInt(day);
        out.writeInt(temperature);
        out.writeUTF(city);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.temperature = in.readInt();
        this.city = in.readUTF();
    }

    @Override
    public String toString() {
        return "TemperatureMapKey{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", city=" + city +
                ", temperature=" + temperature +
                '}';
    }
}
