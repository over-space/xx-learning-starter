package com.learning.basic.poi;

import com.alibaba.fastjson.JSONObject;
import com.learning.BaseTest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 李芳
 * @since 2022/11/3
 */
public class ExcelTest extends BaseTest {

    @Test
    public void test() throws Exception{
        read();
    }

    private void read() throws Exception {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("喔趣");
        AtomicInteger atomicInteger = new AtomicInteger(0);

        List<String> columns = null;

        String fileName = "C:\\Users\\Lee\\OneDrive\\other\\a\\1.txt";
        try(FileReader fileReader = new FileReader(fileName); BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String line;
            while ((line = bufferedReader.readLine()) != null){

                JSONObject jsonObject = JSONObject.parseObject(line);

                columns = columns == null ? jsonObject.keySet().stream().sorted().collect(Collectors.toList()) : columns;

                columns.remove("full_name");
                columns.remove("Real_name");
                columns.remove("first_name");
                columns.remove("last_name");

                columns.add(0, "full_name");

                if(atomicInteger.get() == 0) {
                    Row row = sheet.createRow(atomicInteger.get());
                    for (int i = 0; i < columns.size(); i++) {
                        Cell cell = row.createCell(i, CellType.STRING);
                        cell.setCellValue(columns.get(i));
                    }
                }

                Row row = sheet.createRow(atomicInteger.incrementAndGet());
                for (int i = 0; i < columns.size(); i++) {
                    Cell cell = row.createCell(i, CellType.STRING);
                    cell.setCellValue(jsonObject.getString(columns.get(i)));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        wb.write(new FileOutputStream("C:\\Users\\Lee\\Desktop\\1.xls"));
        wb.close();
    }
}
