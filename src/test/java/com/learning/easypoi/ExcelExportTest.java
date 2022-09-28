package com.learning.easypoi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
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

    private static final String COOKIE = "";
    private static final String URL = "https://hrec.woqu365.com/forward_webfront/api/shared-data/shareddata/api/listPageV2";

    @Test
    public void test() throws IOException {

        File file = new File("C:\\Users\\Lee\\Desktop\\3.txt");
        for (int i = 1; i < 1000; i++) {

            JSONObject jsonObject = get(i, 100);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("listV2");

            if(list == null || list.size() == 0) {
                System.out.println("finished");
                break;
            }

            Collection<String> lines = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                JSONObject row = list.getJSONObject(j);
                lines.add(row.toJSONString());
            }
            FileUtils.writeLines(file, lines, true);
        }
    }

    private JSONObject get(int page, int pageSize) throws IOException {
        String paramStr = "{\"cid\":null,\"operatorUid\":null,\"operatorEid\":null,\"operatorLanguage\":null,\"operatorTimeZone\":null,\"operator\":null,\"paramCid\":60000128,\"categoryBid\":null,\"categoryId\":1200,\"formCategoryId\":null,\"defBid\":null,\"fields\":[\"*\"],\"criteria\":{\"chain\":[{\"code\":\"full_name\",\"opr\":\"LIKE\",\"value\":\"%%\",\"value1\":null,\"inValue\":null}],\"criteria\":null,\"whereSql\":null},\"orderBy\":null,\"groupBy\":null,\"groupByCount\":false,\"pagination\":null,\"distinct\":true,\"ignoreStatus\":null,\"latestVal\":false,\"queryType\":null,\"assistField\":false,\"needFieldCal\":false,\"isSensitive\":null,\"listCode\":null,\"masterDb\":false}\n";

        JSONObject jsonObject = JSON.parseObject(paramStr);

        Map<String, Integer> pagination = Maps.newHashMap();
        pagination.put("page", page);
        pagination.put("pageSize", pageSize);
        jsonObject.put("pagination", pagination);


        Map<String, Object> headers = new HashMap<>();
        headers.put("Cookie", COOKIE);

        String post = HttpClientUtils.post(URL, headers, jsonObject.toJSONString());

        logger.info("result: {}", post);
        return JSON.parseObject(post);
    }
}
