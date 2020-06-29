package com.heyw.ww.my;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class Snow2 {

    protected Seq seq;
    protected long i  = 0l;
    protected long lastTime  = 0l;
    protected short i1 = 0;
    protected short i2 = 0;
    public short shardId = 5;

    protected String name;
    
    protected Snow2(String name) {
        this.name = name;
        seq = Seq.build(name+"-snow2");
        lastTime = System.currentTimeMillis() - 1500000000000l + 1023;
    }

    
    protected static ReentrantReadWriteLock hashLock = new ReentrantReadWriteLock();
    protected static Lock hashWriteLock = hashLock.writeLock();
    protected static HashMap<String, Snow2> map = new HashMap<>();
    public static Snow2 build(String name) {
        Snow2 snow = map.get(name);
        if (snow != null) {
            return snow;
        }
        hashWriteLock.lock();
        try {
            snow = map.get(name);
            if (snow != null) {
                return snow;
            }
            snow = new Snow2(name);
            map.put(name, snow);
        } finally {
            hashWriteLock.unlock();
        }
        return snow;
    }

    public synchronized long gen() {
        long result = -1l;
        long t = System.currentTimeMillis() - 1500000000000l;
        if (lastTime > t) {
            // 补闰秒 或者 防止时间回拨
            i = this.seq.incr((int)(lastTime - t + 1023));
        }
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
        lastTime = t;
        return result;
    }


}
