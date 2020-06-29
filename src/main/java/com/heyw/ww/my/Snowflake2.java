package com.heyw.ww.my;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;


public class Snowflake2 {

    protected Seq seq;
    protected short shardId = 32;

    protected String name;

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected Lock writeLock = lock.writeLock();
    
    protected Snowflake2(String name) {
        this.name = name;
    }
    
    protected static ReentrantReadWriteLock hashLock = new ReentrantReadWriteLock();
    protected static Lock hashWriteLock = hashLock.writeLock();
    protected static HashMap<String, Snowflake2> map = new HashMap<>();
    public static Snowflake2 build(String name) {
        Snowflake2 snow = map.get(name);
        if (snow != null) {
            return snow;
        }
        hashWriteLock.lock();
        try {
            snow = map.get(name);
            if (snow != null) {
                return snow;
            }
            snow = new Snowflake2(name);
            map.put(name, snow);
        } finally {
            hashWriteLock.unlock();
        }
        return snow;
    }

    protected HashMap<Long, short[]> shortArrMap =  new LinkedHashMap<Long, short[]>(16, 1.0f) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, short[]> eldest) {
            return size() > 15;
        }
    };

    
    protected short[] getShortArr(long i) {
        short[] shortArr = shortArrMap.get(i);
        if (shortArr != null) {
            return shortArr;
        }
        shortArr = new short[1024];
        shortArrMap.put(i, shortArr);
        return shortArr;
    }
    
    final short powerOfTwo = 2047; // 4095 (time << 32)
    public synchronized long gen(int times) {
        long result = -1l;
        try {
            long t = System.currentTimeMillis();
            long time = (t >> 10) - 1500000000l;
            short[] shortArr = getShortArr(time);
            short mod = (short)(t & 1023);
            short mircoMod = shortArr[mod];

            if (mircoMod > powerOfTwo) {
                if (times <= 0) {
                    return result;
                }
                Thread.sleep(0, 100000);
                return gen(times - 1);
            }
            shortArr[mod]++;

            return (time << 31) | (mod << 21) | (this.shardId << 11) | ((t & powerOfTwo) ^ mircoMod);
        } catch (Exception e) {
            return gen(times - 1);
        } finally {
        }
    }

    public long gen() {
        return gen(12);
    }
}
