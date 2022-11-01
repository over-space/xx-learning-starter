package com.learning.middleware.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author lifang
 * @since 2021/11/2
 */
public class HbaseTest {

    private Configuration conf;

    private Connection conn;

    private Admin admin;

    private Table table;

    TableName tableName = TableName.valueOf("phone");

    @BeforeTest
    public void init() throws IOException {
        // 创建配置对象
        conf = HBaseConfiguration.create();

        // 设置zookeeper地址
        conf.set("hbase.zookeeper.quorum", "node-03:2181,node-04:2181,node-05:2181");

        // 获取连接
        conn = ConnectionFactory.createConnection(conf);

        admin = conn.getAdmin();

        table = conn.getTable(tableName);
    }

    @Test
    public void testCreateTale() throws IOException {
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);

        ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder("cf".getBytes());

        tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptorBuilder.build());

        if(admin.tableExists(tableName)){
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }

        admin.createTable(tableDescriptorBuilder.build());
    }

    @AfterTest
    public void destory(){
        try {
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
