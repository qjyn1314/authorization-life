package com.authorization.life.auth.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class OauthServerTest {


    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //明文
        String encode = passwordEncoder.encode("3MMoCFo4nTNjRtGZ");

        log.info("密文-{}",encode);

    }



}
