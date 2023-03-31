package com.authorization.utils.test;

import com.authorization.utils.kvp.Kvp;
import com.authorization.utils.kvp.KvpUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 获取
 */
@Slf4j
public class KvpTest {

    public static void main(String[] args) {

        BigDecimal divideApply = KvpUtil.DIVIDE_UP.apply(Kvp.of(new BigDecimal("10"), new BigDecimal("3")));
        System.out.println(divideApply);
        Integer integer = KvpUtil.STR_TO_INT.apply(Kvp.ofVal("", 2));
        BigDecimal bigDecimal = KvpUtil.divideHalfUp(new BigDecimal("0"), new BigDecimal("212"), null);

        System.out.println(bigDecimal);

        int[] nums = new int[]{2, 7, 11, 15};
        int target = 9;

        int[] result = numsSum(nums, target);


    }

    private static int[] numsSum(int[] nums, int target) {
        if ((nums == null || nums.length == 0) || target <= 0) {
            return new int[2];
        }
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            for (int j = 0; j < nums.length; j++) {
                int num2 = nums[j];
                if (num + num2 == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }

    public static double calculate_it() {
        double x = 0.0d;
        double y = 0.0d;
        int total = 0;
        for (int i = 0; i < 7000000; i++) {
            x = Math.random();
            y = Math.random();
            if (Math.sqrt(x * x + y * y) < 1)
                total++;
        }
        return total / 7000000.0;
    }

}
