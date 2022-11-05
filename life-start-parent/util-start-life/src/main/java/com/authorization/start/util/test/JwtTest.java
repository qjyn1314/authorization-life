package com.authorization.start.util.test;

import com.authorization.start.util.jwt.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class JwtTest {


    public static void main(String[] args) {
        String accessToken = "k6fYZ04y8Om2OXqnSV1rGcwJhs50UES5zXvgKDO-nTQkRFCh4U00dGONtmv6J0gEhqLYegxPcWAZN2Y40NteabPwfHnS2k8TA9ASPrfYPMpJwHbpJHFbdYaMilcjqRYP";
        Map<String, Object> map = Jwts.parse(accessToken).getPayload().toJSONObject();

        log.info("accessTokenMap-{}",map);

    }


}
