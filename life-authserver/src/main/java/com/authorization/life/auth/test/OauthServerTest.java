package com.authorization.life.auth.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class OauthServerTest {

    public static String jwtToken = "eyJraWQiOiI1NzQ1NzkwNy01ZjlhLTQ4ZjMtYTM0Ny00ZjA3ZTQ4NzRhZDgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhdXRoLXNlcnZlciIsImF1ZCI6InBhc3Nwb3J0IiwibmJmIjoxNzI0NTE2NzIxLCJzY29wZSI6WyJURU5BTlQiXSwiaXNzIjoiaHR0cHM6Ly9hdXRob3JpemF0aW9uLmxpZmUiLCJleHAiOjE3MjQ2MDMxMjEsImlhdCI6MTcyNDUxNjcyMSwianRpIjoiMjViYmQwMzQtNTA1NS00MjJiLWFhMzEtNjMyNzRiM2U4NzQxIiwidG9rZW4iOiIwNjc3ODczZGY1YzA0ZmRlYTgxNzBhMjU5MjEyODRjMyJ9.I2IhscWdedBFNq2emL0DfmL1b2hn7nIvBw8psHClLqiC1jv4mjYSB7pU9cvMzgH6oniaAI7ItetiKOCbC48NdFHAjIpMdbTrnUeo87q44Qyu1P3_M5aQJv_ZVgvgnX1jnOcybW2xHRB-qn6VJYMmX9L5q36XtFnSDJ7z5KrU6IVaJjc_ZHzoE-mQNbgqa_kqMIF47E7O_Io4x9LEzimhHQiEpZMgDssDv3Zsg7sCzMg72RV1EUNSwzjRIzII23g55-NrmFKgz-_mh9KIXYzyfVzbghE52eNw-rUE3v0u6pcDRQ6k8QQv9kSnCGq3ZjLubFrbR2zo51FHtr3YdV1Z_g";


    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //明文
        String encode = passwordEncoder.encode("admin");

        log.info("密文-{}", encode);

//        JwtDecoder jwtDecoder = OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource());
//        Jwt decode = jwtDecoder.decode(jwtToken);
//        Map<String, Object> claims = decode.getClaims();
//        log.info("claims: {}", claims);

    }

//    static public JWKSource<SecurityContext> jwkSource() {
//        RSAKey rsaKey = Jwks.generateRsa();
//        JWKSet jwkSet = new JWKSet(rsaKey);
//        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//    }


}
