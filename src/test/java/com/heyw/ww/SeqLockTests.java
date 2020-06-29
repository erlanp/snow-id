package com.heyw.ww;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.io.BufferedOutputStream;
import java.io.Writer;

import java.io.IOException;

import com.heyw.ww.my.Seq;
import com.heyw.ww.my.SeqLock;
import com.heyw.ww.my.SeqSync;
import com.heyw.ww.my.SeqAtomic;
import com.heyw.ww.my.Snow;


@SpringBootTest
class SeqLockTests {

    String type = "SeqSync";
    // String type = "SeqLock";
    // String type = "SeqAtomic";
    // String type = "Seq";


    @Test
    void seqThread() {

        new Thread() {
            @Override
            public void run() {
                
                Seq seq = Seq.build("hello", type);
                
                System.out.println("seq x1 start: " + seq.curr());
                for (int i = 0; i < 1_00000; i++) {
                    // try {
                    //     Thread.sleep(0);
                    // } catch (InterruptedException e) {
                    //     e.printStackTrace();
                    // }
                    seq.incr();
                }

                System.out.println(Thread.currentThread().getId() + "seq x2 end: " + seq.curr());
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                Seq seq = Seq.build("hello", type);
                
                System.out.println("seq x2 start: " + seq.curr());
                for (int i = 0; i < 1_00000; i++) {
                    // try {
                    //     Thread.sleep(0);
                    // } catch (InterruptedException e) {
                    //     e.printStackTrace();
                    // }
                    seq.incr();
                }
                System.out.println(Thread.currentThread().getId() + "seq x2 end: " + seq.curr());
                
            }
        }.start();
        
        long start2 = System.nanoTime();
        long start = System.currentTimeMillis();

        Seq seq = Seq.build("hello", type);

        System.out.println("seq main start: " + seq.curr());
        for (int i = 0; i < 8_00000; i++) {
            seq.incr();
        }
        System.out.println(Thread.currentThread().getId() + "seq main end: " + seq.curr());

        long time2 = System.nanoTime() - start2;
        System.out.println(time2);

        long time = System.currentTimeMillis() - start;
        System.out.println(time);
        
    }

}
