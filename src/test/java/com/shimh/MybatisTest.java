package com.shimh;

import com.shimh.mapper.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shimh
 * @create: 2019年11月
 **/
public class MybatisTest extends Test{

    @Autowired
    DataMapper dataMapper;

    @org.junit.Test
    public void test() {
        long start = System.currentTimeMillis();
        dataMapper.get();
        System.out.println((System.currentTimeMillis() - start));
    }
}
