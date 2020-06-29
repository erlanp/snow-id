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


import com.heyw.ww.my.Snowflake;


@SpringBootTest
class SnowflakeTests {

    @Test
    void tea() {
        long i = 0l;
        long i2 = 0l;
        Snowflake set = Snowflake.build("hey");
        // Snowflake set_yoo = Snowflake.build("yoo");
        // Snowflake set_test = Snowflake.build("test");

        int max = 10;
        Snowflake[] set_array = new Snowflake[max];
        Integer key = 0;
        for (; key<max; key++) {
            set_array[key] = Snowflake.build(key.toString()); 
        }

        HashMap<Long, Boolean> map = new HashMap<>(2000000, 1.0f);
        long time = System.currentTimeMillis();
        for (int j=0; j<1000000; j++) {
            for (key = 0; key < max; key++) {
                i2 = set_array[key].gen(); 
            }
            map.put(i2, true);
            if (i2 == -1l) {
                i++;
            }
        }
        System.out.println(System.currentTimeMillis() - time);
        System.out.println(set.gen());
        System.out.println(set.gen());
        System.out.println("map:" + map.size());
        System.out.println(i);
    }

}
