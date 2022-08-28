package com.authorization.life.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 用于测试security中的必要测试信息
 */
@Slf4j
public class SecurityTest {


    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //明文
        String encode = passwordEncoder.encode("123456");

        log.info("密文-{}",encode);

    }


}
