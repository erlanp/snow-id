package com.heyw.ww.my;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.HashMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class Seq {

    protected static ReentrantReadWriteLock hashLock = new ReentrantReadWriteLock();
    protected static Lock hashWriteLock = hashLock.writeLock();

    protected long i = 0l;
    protected short logCnt = 128;
    protected long diskValue = 0l;

    protected String name;
    public String getName() {
        return name;
    }

    public RandomAccessFile bos = null;
    public FileInputStream input = null;

    protected Seq(String name) {
        init(name);
    }

    protected void init(String name) {
        this.name = name;
        diskValue = diskGet();
        
        if (diskValue > i) {
            i = diskValue;
        }
    }
    
    protected static HashMap<String, Seq> dict = new HashMap<>();

    public static Seq build(String name, String className) {
        String path = className + "-" + name;
        Seq seq = null;
        seq = dict.get(path);
        if (seq != null) {
            return seq;
        }
        hashWriteLock.lock();
        try {
            seq = dict.get(path);
            if (seq != null) {
                return seq;
            }
            switch (className) {
                case "Seq":
                    seq = new Seq(path);
                break;
                case "SeqLock":
                    seq = new SeqLock(path);
                break;
                case "SeqAtomic":
                    seq = new SeqAtomic(path);
                break;
                case "SeqSync":
                    seq = new SeqSync(path);
                break;
            }
            dict.put(path, seq);
        } finally {
            hashWriteLock.unlock();
        }
        return seq;
    }

    public static Seq build(String name) {
        return build(name, "Seq");
    }

    public long incr() {
        return incr((short)1);
    }

    public long incr(short delta) {
        i += delta;
        if (i >= diskValue) {
            diskValue = i + logCnt;
            diskSet(diskValue);
        }
        return i;
    }

    public long incr(int delta) {
        i += delta;
        if (i >= diskValue) {
            diskValue = i + logCnt;
            diskSet(diskValue);
        }
        return i;
    }

    public long curr() {
        return i;
    }

    public long diskGet() {
        try {
            if (input == null) {
                input = new FileInputStream("./"+name+".txt");
            }
            byte[] bytes = new byte[8];
            input.read(bytes);
            return getLong(bytes);
        } catch (IOException e) {
            return -1l;
        }
    }

    public void diskSet(long value) {
        try {
            if (bos == null) {
                bos = new RandomAccessFile("./"+name+".txt", "rw");
            }
            bos.seek(0);
            bos.write(putLong(value));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            
        }
    }

    protected static long getLong(byte[] b) {
        return ((b[7] & 0xFFL)      ) +
               ((b[6] & 0xFFL) <<  8) +
               ((b[5] & 0xFFL) << 16) +
               ((b[4] & 0xFFL) << 24) +
               ((b[3] & 0xFFL) << 32) +
               ((b[2] & 0xFFL) << 40) +
               ((b[1] & 0xFFL) << 48) +
               (((long) b[0])      << 56);
        
    }

    protected static byte[] putLong(long val) {
        byte[] b = new byte[8];
        b[7] = (byte) (val       );
        b[6] = (byte) (val >>>  8);
        b[5] = (byte) (val >>> 16);
        b[4] = (byte) (val >>> 24);
        b[3] = (byte) (val >>> 32);
        b[2] = (byte) (val >>> 40);
        b[1] = (byte) (val >>> 48);
        b[0] = (byte) (val >>> 56);
        return b;
    }

    protected void finalize() {
        try {
            if (bos != null) {
                bos.close();  
            }
        } catch (IOException e) {  
            e.printStackTrace();  
        }

        try {
            if (input != null) {
                input.close();  
            }
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }

}
