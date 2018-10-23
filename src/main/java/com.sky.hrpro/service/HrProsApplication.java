package com.sky.hrpro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


/**
 * @author CarryJey
 * @date 2018年9月27日 上午9:36:42
 */

@SpringBootApplication
public class HrProsApplication{

    public static void main(String args[]){
        SpringApplication.run(HrProsApplication.class, args);
    }

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker(@Value("${app.snowflake.workerId:0}") int workerId,
                                               @Value("${app.snowflake.dataCenterId:0}") int dataCenterId) {
        return new SnowflakeIdWorker(workerId, dataCenterId);
    }
}