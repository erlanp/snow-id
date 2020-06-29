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
import java.util.HashMap;

import java.io.IOException;

import com.heyw.ww.my.Seq;


@SpringBootTest
class SeqTests {

    @Test
    void tea() {
        long i = 0l;
        long i2 = 0l;
        // Seq set = Seq.build("hey", "SeqLock");
        Seq set = Seq.build("hey", "SeqSync");

        long time = System.currentTimeMillis();
        HashMap<Long, Boolean> map = new HashMap<>(1000000, 1.0f);
        for (int j=0; j<1000000; j++) {
            i2 = set.incr();
            // map.put(i2, true);
            if (i2 == -1l) {
                i++;
            }
        }
        System.out.println(System.currentTimeMillis() - time);
        System.out.println(set.incr());
        System.out.println(set.incr());
        System.out.println("map:" + map.size());
        System.out.println(i);
    }

}
