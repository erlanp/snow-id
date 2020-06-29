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

import com.heyw.ww.my.Snow;


@SpringBootTest
class SnowTests {

    @Test
    void tea() {
        long i = 0l;
        long i2 = 0l;
        Snow set = Snow.build("hey");

        int max = 10;
        Snow[] set_array = new Snow[max];
        Integer key = 0;
        for (; key<max; key++) {
            set_array[key] = Snow.build(key.toString()); 
        }

        int k = 1000000;
        HashMap<Long, Boolean> map = new HashMap<>(k, 1.0f);
        long time = System.currentTimeMillis();
        for (int j=0; j<k; j++) {
            for (key = 0; key < max; key++) {
                i2 = set_array[key].gen(); 
            }
            // map.put(i2, true);
            if (i2 == -1l) {
                i++;
            }
        }
        long time2 = System.currentTimeMillis() - time;
        System.out.println("use mircotime:" + time2);
        System.out.println(set.gen());
        System.out.println(set.gen());
        System.out.println("map:" + map.size());
        System.out.println("tps = " + ((k-i) * max / time2 * 1000));
        System.out.println(i);
    }


}
