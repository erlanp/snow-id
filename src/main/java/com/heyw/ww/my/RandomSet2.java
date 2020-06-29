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
import java.util.BitSet;
import java.util.Random;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class RandomSet2 {

    protected short shardId = 5;
    final int million = 1048576;
    final int million2 = 1048575;

    protected String name;
    protected Random random = new SecureRandom();
    
    protected RandomSet2(String name) {
        this.name = name;
    }
    
    protected static ReentrantReadWriteLock hashLock = new ReentrantReadWriteLock();
    protected static Lock hashWriteLock = hashLock.writeLock();
    protected static HashMap<String, RandomSet2> map = new HashMap<>();
    public static RandomSet2 build(String name) {
        RandomSet2 randomSet = map.get(name);
        if (randomSet != null) {
            return randomSet;
        }
        hashWriteLock.lock();
        try {
            randomSet = map.get(name);
            if (randomSet != null) {
                return randomSet;
            }
            randomSet = new RandomSet2(name);
            map.put(name, randomSet);
        } finally {
            hashWriteLock.unlock();
        }
        return randomSet;
    }

    protected HashMap<Long, BitSet> bitSetMap = new LinkedHashMap<Long, BitSet>(16, 1.0f) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, BitSet> eldest) {
            return size() > 15;
        }
    };
    protected BitSet getBitSet(long i) {
        BitSet bitSet = bitSetMap.get(i);
        if (bitSet != null) {
            return bitSet;
        }
        bitSet = new BitSet(million);
        bitSetMap.put(i, bitSet);
        return bitSet;
    }

    public synchronized long gen(int times) {
        long result = -1l;
        try {
            long time = (System.currentTimeMillis() >> 9) - 1500000000l;
            BitSet bitSet = getBitSet(time);
            int nano = (int)(System.nanoTime() & million2);
            if (bitSet.get(nano) == false) {
                bitSet.set(nano);
            } else if (times <= 0) {
                return result;
            } else {
                Boolean call = true;
                int nano2 = 0;
                for (int j=1; j<=10; j++) {
                    nano2 = random.nextInt(million);
                    if (bitSet.get(nano2) == false) {
                        bitSet.set(nano2);
                        nano = nano2;
                        call = false;
                        break;
                    }
                    nano = (nano + 1) & million2;
                    if (bitSet.get(nano) == false) {
                        bitSet.set(nano);
                        call = false;
                        break;
                    }
                }
                if (call) {
                    return gen(times - 1);
                }
            }
            result = (time << 30) | (shardId << 20) | nano;
            return result;
        } catch (Exception e) {
            return gen(times - 1);
        } finally {
        }
    }

    public long gen() {
        return gen(10);
    }


}
