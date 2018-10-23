package com.sky.hrpro.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.sky.hypro.service.Idservice.IdService;
import com.sky.hypro.service.Idservice.InvalidSystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;

/**
 * @Author: CarryJey
 * @Date: 2018/10/23 13:09:32
 */
@Service(version = "1.0.0")
public class IdServiceImpl implements IdService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private long counter = 0;

    @Autowired
    private SnowflakeIdWorker idWorker;

    @Override
    public long snowflakeNextId() throws InvalidSystemClock {
        return idWorker.nextId();
    }

    @Override
    public long[] snowflakeNextIds(int count) throws InvalidSystemClock {
        // 不需要精确，并发没有问题
        counter++;
        if (counter % 100 == 0) {
            logger.info("nextIds, count: {}.", count);
        }

        return idWorker.nextIds(count);
    }
}
