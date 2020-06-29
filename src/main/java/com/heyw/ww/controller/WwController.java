package com.heyw.ww.controller;

import com.heyw.ww.my.Seq;
import com.heyw.ww.my.Snow;
import com.heyw.ww.my.Snowflake2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;
import java.util.BitSet;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class WwController {
	@RequestMapping("/")
    public String index(String mess) {
        if (mess == null) {
            mess = "";
        }
        return "Hello Spring Boot 2.2.6!" + mess;
    }

	@RequestMapping("/index2")
    public static String index2(String mess) {
        if (mess == null) {
            mess = "";
        }
        return "Hello Spring Boot 2.2.6!" + mess;
    }

    @RequestMapping("/snowflake_list")
    public List<Long> snowflakeList(Long id, Short limit) {
        Snowflake2 snow = Snowflake2.build(getId(id));
        List<Long> snowList = new ArrayList<>();
        for (int i=0; i<getLimit(limit); i++) {
            snowList.add(snow.gen());
        }
        return snowList;
    }

    @RequestMapping("/snowflake_list2")
    public Long[] snowflakeList2(Long id, Short limit) {
        Snowflake2 snow = Snowflake2.build(getId(id));
        limit = getLimit(limit);
        Long[] snowList = new Long[limit];
        for (int i=0; i<limit; i++) {
            snowList[i] = snow.gen();
        }
        return snowList;
    }

    @RequestMapping("/snowflake")
    public long snowflake(Long id) {
        Snowflake2 snow = Snowflake2.build(getId(id));
        return snow.gen();
    }

    @RequestMapping("/snow_list")
    public List<Long> snowList(Long id, Short limit) {
        Snow snow = Snow.build(getId(id));
        List<Long> snowList = new ArrayList<>();
        for (int i=0; i<getLimit(limit); i++) {
            snowList.add(snow.gen());
        }
        return snowList;
    }

    @RequestMapping("/snow")
    public long snow(Long id) {
        Snow snow = Snow.build(getId(id));
        return snow.gen();
    }
    
    @RequestMapping("/seq_list")
    public List<Long> seqList(Long id, Short limit) {
        Seq seq = Seq.build(getId(id), "SeqSync");
        List<Long> seqList = new ArrayList<>();
        long curr = seq.incr(limit);
        seqList.add(curr - limit);
        seqList.add(curr);
        return seqList;
    }


    @RequestMapping("/seq")
    public long seq(Long id) {
        Seq seq = Seq.build(getId(id));
        return seq.incr();
    }

    protected static String getId(Long id) {
        if (id == null) {
            id = 0L;
        }
        return id.toString();
    }

    protected static short getLimit(Short limit) {
        if (limit == null || limit == 0) {
            return (short)100;
        } else {
            return (short)Math.min(Math.abs(limit), 4095);
        }
    }
}
