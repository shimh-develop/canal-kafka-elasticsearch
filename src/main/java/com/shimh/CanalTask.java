package com.shimh;

import com.shimh.canal.client.SimpleCanalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author: shimh
 * @create: 2019年10月
 **/
@Component
public class CanalTask implements ApplicationRunner {

    @Autowired
    private SimpleCanalClient canalClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        canalClient.start();

    }
}
