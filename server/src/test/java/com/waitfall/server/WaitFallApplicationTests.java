package com.waitfall.server;

import cn.hutool.crypto.digest.BCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WaitFallApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void generatePassword() {

        String hashpw = BCrypt.hashpw("123456");

        System.out.println(hashpw);

        boolean checkpw = BCrypt.checkpw("123456", hashpw);

        System.out.println(checkpw);
    }

}
