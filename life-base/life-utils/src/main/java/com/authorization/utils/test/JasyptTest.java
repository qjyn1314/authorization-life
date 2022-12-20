package com.authorization.utils.test;

import com.authorization.utils.jasypt.JasyptUtils;

/**
 * TODO 请填写类描述
 *
 * @author wangjunming
 * @date 2022/12/20 15:39
 */
public class JasyptTest {

      public static void main(String[] args) {
        String message = "123456";
        String password = JasyptUtils.SECRET_KEY;

        //一个同样的密码和秘钥，每次执行加密，密文都是不一样的。但是解密是没问题的。
        String jasyptEncrypt = JasyptUtils.stringEncryptor(password, message, true);
        System.out.println(jasyptEncrypt);

        String jasyptEncrypt1 = JasyptUtils.stringEncryptor(password, "", false);
        System.out.println(jasyptEncrypt1);
    }

}