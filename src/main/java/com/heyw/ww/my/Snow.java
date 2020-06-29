package com.heyw.ww.my;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class Snow {

    protected Seq seq;
    protected long i  = 0l;
    protected short i1 = 0;
    protected short i2 = 0;
    public short shardId = 5;

    protected String name;

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected Lock writeLock = lock.writeLock();
    
    protected Snow(String name) {
        this.name = name;
        seq = Seq.build(name+"-snow");
    }

    
    protected static ReentrantReadWriteLock hashLock = new ReentrantReadWriteLock();
    protected static Lock hashWriteLock = hashLock.writeLock();
    protected static HashMap<String, Snow> map = new HashMap<>();
    public static Snow build(String name) {
        Snow snow = map.get(name);
        if (snow != null) {
            return snow;
        }
        hashWriteLock.lock();
        try {
            snow = map.get(name);
            if (snow != null) {
                return snow;
            }
            snow = new Snow(name);
            map.put(name, snow);
        } finally {
            hashWriteLock.unlock();
        }
        return snow;
    }

    public long gen() {
        writeLock.lock();
        long result = -1l;
        try {
            long t = System.currentTimeMillis() - 1500000000000l;
            if ((t % 2l) == 1l) {
                if (this.i1 == 0) {
                    this.i2 = 0;
                }
                this.i1++;
                if (this.i1 >= 256) {
                    this.i1 = 0;
                    this.i2 = 0;
                    i = this.seq.incr();
                }
                result = (t + i * 2l) << 16 | (this.shardId << 8) | ((t & 255) ^ this.i1);
            } else {
                if (this.i2 == 0) {
                    this.i1 = 0;
                }
                this.i2++;
                if (this.i2 >= 256) {
                    this.i2 = 0;
                    this.i1 = 0;
                    i = this.seq.incr();
                }
                result = (t + i * 2l) << 16 | (this.shardId << 8) | ((t & 255) ^ this.i2);
            }
        } finally {
            writeLock.unlock();
        }
        return result;
    }


}
