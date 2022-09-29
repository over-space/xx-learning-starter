package com.learning.easypoi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.learning.BaseTest;
import com.learning.http.HttpClientUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李芳
 * @since 2022/9/26
 */
public class ExcelExportTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ExcelExportTest.class);

    @Test
    public void test() throws IOException {

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("title", "张三");
        map.put("age", 30);
        list.add(map);

        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);

        FileOutputStream fos = new FileOutputStream("C:\\Users\\Lee\\Desktop\\test.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
