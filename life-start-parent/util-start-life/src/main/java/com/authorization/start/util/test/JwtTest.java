package com.authorization.start.util.test;

import com.authorization.start.util.jwt.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class JwtTest {


    public static void main(String[] args) {
        String accessToken = "eyJraWQiOiIyMDk0ODEzZi02YjJlLTQyMmEtOTdjNi0wNzJmNWVjYTYwNjciLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhdXRoLXNlcnZlciIsImF1ZCI6InBhc3Nwb3J0IiwibmJmIjoxNjY3NzIzNTc1LCJzY29wZSI6WyJURU5BTlQiXSwiaXNzIjoiaHR0cHM6XC9cL2F1dGhvcml6YXRpb24ubGlmZSIsImV4cCI6MTY2NzgwOTk3NSwiaWF0IjoxNjY3NzIzNTc1LCJ0b2tlbiI6Ijg4NjM2NGViNzYyODQzZDY5MzIxMWJkMjYwMTk1NzBiIn0.eNkCKiYte9rj8O0eAQw2Jg3pjH8mW9nEC_lEDL6LGeuAcPa2Iq2DPqrn6065ZuJrQ9nZ-UicZO8675pR5aEH0qw0ZUdC4RPltWAfB9xTPOmXVySFDT54jS9bnlKshdSOeFes1e_YrgHciLXuODN4weGgZ__c_RJhy4U_bmWrQvajn6j-UeLV0KH1zU4r5_lLVGFsj_6A8Ys4ps59uEHH3ZBfxQTLj1KxOb7KZXoP1n5Ml8YtoTiLI9qC0Rv0_sNKZ7lnAYTJD-dy94R2zkRTC2X7Edw6MQEpP30SBDD6E452dk5kcU0MdaE-nRAInlTyiV0eBXhwsS8ujWbPoZVAZw";
        Map<String, Object> map = Jwts.parse(accessToken).getPayload().toJSONObject();

        log.info("accessTokenMap-{}",map);

    }


}
