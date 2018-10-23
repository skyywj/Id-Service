package com.sky.hrpro.service;

import com.sky.hypro.service.Idservice.InvalidSystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: CarryJey
 * @Date: 2018/10/23 13:09:51
 * desc:SnowFlake java算法实现
 */
public class SnowflakeIdWorker {
    private static final Logger log = LoggerFactory.getLogger(SnowflakeIdWorker.class);

    /**
     * 以 2018-09-09T08:29:40.580Z 作为开端
     */
    private static final long TW_EPOCH = 1536481780580L;

    private static final long WORKER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    private static final long SEQUENCE_BITS = 12L;
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    private long lastTimestamp = -1L;
    private long sequence = 0;


    private final long workerId;
    private final long dataCenterId;

    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        // sanity check for workerId
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }

        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;

        log.info("worker starting. timestamp left shift {}, datacenter id bits {}, worker id bits {}, sequence bits {}, workerid {}",
                TIMESTAMP_LEFT_SHIFT, DATA_CENTER_ID_BITS, WORKER_ID_BITS, SEQUENCE_BITS, workerId);

    }

    public long getWorkerId() {
        return workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    public synchronized long nextId() {
        return nextId0();
    }

    public synchronized long[] nextIds(int count) {
        if (count < 0) {
            count = 1;
        }

        if (count > 200) {
            count = 200;
        }

        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = nextId0();
        }

        return ids;
    }

    private long nextId0() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            log.error("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new InvalidSystemClock(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return ((timestamp - TW_EPOCH) << TIMESTAMP_LEFT_SHIFT) | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
    }


    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
