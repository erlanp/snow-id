package com.heyw.ww.my;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import java.util.concurrent.atomic.AtomicLong;

public class SeqAtomic extends Seq {

    protected SeqAtomic(String name) {
        super(name);
    }

    protected AtomicLong j = new AtomicLong(diskValue);

    @Override
    protected void init(String name) {
        this.name = name;
        diskValue = diskGet();

        j = new AtomicLong(diskValue);
        System.out.println("Atomic");
    }

    @Override
    public long incr(short delta) {
        
        if (j.addAndGet(delta) >= diskValue) {
            diskValue = j.get() + logCnt;
            diskSet(diskValue);
        }
        return j.get();
    }

    @Override
    public long curr() {
        return j.get();
    }

}
