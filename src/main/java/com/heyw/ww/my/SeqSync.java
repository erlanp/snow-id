package com.heyw.ww.my;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class SeqSync extends Seq {

    protected SeqSync(String name) {
        super(name);
    }

    @Override
    public synchronized long incr(short delta) {
        return super.incr(delta);
    }

}
