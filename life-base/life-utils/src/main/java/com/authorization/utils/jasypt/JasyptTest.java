package com.authorization.utils.jasypt;

import lombok.extern.slf4j.Slf4j;

/**
 * Jasypt 对密码的加解密的测试类
 *
 * @author wangjunming
 * @date 2022/12/20 15:39
 */
@Slf4j
public class JasyptTest {

    public static void main(String[] args) {
        String message = "EUc57AF7EC5KptLp";
        String password = JasyptUtils.SECRET_KEY;

        //一个同样的密码和秘钥，每次执行加密，密文都是不一样的。但是解密是没问题的。
        String jasyptEncrypt = JasyptUtils.stringEncryptor(password, message, true);
        log.info("加密后的字符串-{}", jasyptEncrypt);

        String jasyptEncrypt1 = JasyptUtils.stringEncryptor(password, jasyptEncrypt, false);
        log.info("解密后的字符串-{}", jasyptEncrypt1);

    }

}
