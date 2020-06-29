package com.heyw.ww.my;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class SeqLock extends Seq {

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected Lock writeLock = lock.writeLock();

    protected SeqLock(String name) {
        super(name);
    }

    @Override
    public long incr(short delta) {
        writeLock.lock();
        try {
            return super.incr(delta);
        } finally {
            writeLock.unlock();
        }
    }

}
