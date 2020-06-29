package com.heyw.ww.my;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.BitSet;
import java.util.Random;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class RandomSet {

    protected Seq seq;
    protected Long i  = 0l;
    protected Long i1 = 0l;
    protected Long i2 = 0l;
    protected Long shardId = 5l;

    protected String name;
    protected Random random = new SecureRandom();

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected Lock writeLock = lock.writeLock();
    
    protected RandomSet(String name) {
        this.name = name;
    }
    
    protected static ReentrantReadWriteLock hashLock = new ReentrantReadWriteLock();
    protected static Lock hashWriteLock = hashLock.writeLock();
    protected static HashMap<String, RandomSet> map = new HashMap<>(32);
    public static RandomSet build(String name) {
        hashWriteLock.lock();
        RandomSet randomSet = null;
        try {
            randomSet = map.get(name);
            if (randomSet != null) {
                return randomSet;
            }
            randomSet = new RandomSet(name);
            map.put(name, randomSet);
        } finally {
            hashWriteLock.unlock();
        }
        return randomSet;
    }

    protected LinkedHashMap<Long, BitSet> bitSetMap = new LinkedHashMap<Long, BitSet>(16000, 1.0f) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, BitSet> eldest) {
            return size() > 15999;
        }
    };
    protected BitSet getBitSet(long i) {
        BitSet bitSet = bitSetMap.get(i);
        if (bitSet != null) {
            return bitSet;
        }
        bitSet = new BitSet(1024);
        bitSetMap.put(i, bitSet);
        return bitSet;
    }

    public Long gen(int times) {
        writeLock.lock();
        Long result = -1l;
        try {
            long time = System.currentTimeMillis() - 1500000000000l;
            BitSet bitSet = getBitSet(time);
            int nano = (int)(System.nanoTime() & 1023);
            if (bitSet.get(nano) == false) {
                bitSet.set(nano);
            } else if (times <= 0) {
                return result;
            } else {
                Boolean call = true;
                int nano2 = 0;
                for (int j=1; j<=10; j++) {
                    nano = (nano + 1) & 1023;
                    if (bitSet.get(nano) == false) {
                        bitSet.set(nano);
                        call = false;
                        break;
                    }
                    nano2 = random.nextInt(1024);
                    if (bitSet.get(nano2) == false) {
                        bitSet.set(nano2);
                        nano = nano2;
                        call = false;
                        break;
                    }
                }
                if (call) {
                    // Thread.sleep(0, 10);
                    return gen(times - 1);
                }
            }
            result = (time << 10) | nano;
            return result;
        } catch (Exception e) {
            return gen(times - 1);
        } finally {
            writeLock.unlock();
        }
    }

    public Long gen() {
        return gen(10);
    }


}
