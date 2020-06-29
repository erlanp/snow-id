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
import java.util.HashMap;
import java.util.HashSet;


import com.heyw.ww.my.RandomSet2;


@SpringBootTest
class RandomSet2Tests {

    @Test
    void tea() {
        long i = 0l;
        long i2 = 0l;
        RandomSet2 set = RandomSet2.build("hey");

        int max = 40;
        RandomSet2[] set_array = new RandomSet2[max];
        Integer key = 0;
        for (; key<max; key++) {
            set_array[key] = RandomSet2.build(key.toString()); 
        }

        HashMap<Long, Boolean> map = new HashMap<>(2000000, 1.0f);
        long time = System.currentTimeMillis();
        int k = 1000000;
        for (int j=0; j<k; j++) {
            for (key = 0; key < max; key++) {
                i2 = set_array[key].gen(); 
            }
            if (i2 == -1l) {
                System.out.println("err-j:" + j);
                i++;
            } else {
                // map.put(i2, true);
            }
        }
        long time2 = System.currentTimeMillis() - time;
        System.out.println("use mircotime:" + time2);
        System.out.println("set.gen():" + set.gen());
        System.out.println("set.gen():" + set.gen());
        System.out.println("map:" + map.size());
        System.out.println("tps = " + ((k-i) * max / time2 * 1000));
        System.out.println("error lost:" + i);
    }

}
