package com.learning.seckill.controller;

import com.learning.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lifang
 * @since 2022/3/21
 */
@RestController
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping(value = "/mysql/seckill")
    public ResponseEntity seckillByMySQL(@RequestParam String goodsNum) {
        goodsService.seckillByMySQL(goodsNum);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping(value = "/redis/seckill/v1")
    public ResponseEntity seckillByRedisV1(@RequestParam String goodsNum) {
        goodsService.seckillByRedisV1(goodsNum);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/redis/seckill/v2")
    public ResponseEntity seckillByRedisV2(@RequestParam String goodsNum) {
        goodsService.seckillByRedisV2(goodsNum);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/thread/seckill/v1")
    public ResponseEntity seckillByThreadV1(@RequestParam String goodsNum) {
        goodsService.seckillByThreadV1(goodsNum);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping(value = "/init/goods")
    public ResponseEntity init() {
        goodsService.init();
        return new ResponseEntity(HttpStatus.OK);
    }

}
