package com.sky.hrpro.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sky.hypro.service.Idservice.IdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.invoke.MethodHandles;

/**
 * @Author: CarryJey @Date: 2018/10/23 13:29:21
 */
@SpringBootTest
@Component
@RunWith(SpringRunner.class)
public class IdServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Reference(version = "1.0.0")
    public IdService idService;

    @Test
    public void testNextId(){
        logger.info("nextId: {}.", idService.snowflakeNextId());
    }

    @Test
    public void testNextIds(){
        logger.info("nextIds: {}.", idService.snowflakeNextIds(300));
    }
}
