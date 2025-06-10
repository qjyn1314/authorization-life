package com.authorization.life.auth.app.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.authorization.life.auth.app.constant.RedisKeyValid;
import com.authorization.life.auth.app.dto.LifeUserDTO;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.app.vo.LifeUserVO;
import com.authorization.life.auth.infra.entity.LifeUser;
import com.authorization.life.auth.infra.mapper.UserMapper;
import com.authorization.redis.start.util.RedisCaptchaValidator;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.valid.start.group.SaveGroup;
import com.authorization.valid.start.group.ValidGroup;
import com.authorization.valid.start.util.ValidUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<LifeUserVO> page(LifeUserDTO lifeUser) {
        return PageHelper.startPage(lifeUser.getPageNo(), lifeUser.getPageSize())
                .doSelectPageInfo(() -> handleUserPage(lifeUser));
    }

    private List<LifeUserVO> handleUserPage(LifeUserDTO lifeUser) {
        List<LifeUser> userList = mapper.page(Convert.convert(LifeUser.class, lifeUser));
        return userList.stream().map(item -> Convert.convert(LifeUserVO.class, item)).collect(Collectors.toList());
    }

    /**
     * 登录查询使用
     *
     * @param username 手机号/邮箱
     */
    @Override
    public LifeUser selectByUsername(String username) {
        return mapper.selectOne(Wrappers.lambdaQuery(LifeUser.class)
                .or().eq(LifeUser::getPhone, username)
                .or().eq(LifeUser::getUsername, username)
                .or().eq(LifeUser::getEmail, username));
    }

    /**
     * 锁定用户几小时
     *
     * @param userId   用户ID
     * @param lockTime 锁定时间-单位：小时；
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lock(String userId, Integer lockTime) {
        Assert.notBlank(userId, "用户ID不能为空.");
        Assert.notNull(lockTime, "锁定时长(小时)不能为空.");
        LifeUser lifeUser = mapper.selectOne(Wrappers.lambdaQuery(new LifeUser()).eq(LifeUser::getUserId, userId));
        Assert.notNull(lifeUser, "未找到该用户信息");
        LifeUser updateLifeUser = new LifeUser();
        updateLifeUser.setUserId(userId);
        updateLifeUser.setLockedFlag(Boolean.TRUE);
        updateLifeUser.setLockedTime(LocalDateTime.now().plusHours(lockTime));
        mapper.updateById(updateLifeUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String emailRegister(LifeUserDTO lifeUser) {
        Assert.notNull(lifeUser, "注册用户不能为空.");
        Assert.notBlank(lifeUser.getEmail(), "邮箱不能为空.");
        ValidUtil.validateAndThrow(lifeUser, SaveGroup.class);
        Boolean emailExist = validateEmailExist(lifeUser);
        Assert.isFalse(emailExist, "该邮箱已注册.");
        Assert.notBlank(lifeUser.getHashPassword(), "密码不能为空.");
        boolean verifiedExpirationDate = RedisCaptchaValidator.verifyExpirationDate(redisUtil, lifeUser.getCaptchaUuid(), lifeUser.getCaptchaCode());
        Assert.isTrue(verifiedExpirationDate, "验证码已过期, 请重新获取.");
        boolean verify = RedisCaptchaValidator.verify(redisUtil, lifeUser.getCaptchaUuid(), lifeUser.getCaptchaCode());
        Assert.isTrue(verify, "验证码不正确,请重新输入.");

        //此处是验证通过, 将删除缓存中的验证码和邮箱校验逻辑
        RedisKeyValid.delEmailRepeatSendCode(redisUtil, lifeUser.getEmail());
        RedisKeyValid.delEmailRegSendCode(redisUtil, lifeUser.getCaptchaUuid());


        LifeUser insertUser = new LifeUser();
        insertUser.setUsername(getUserName());
        insertUser.setEmail(lifeUser.getEmail());
        insertUser.setEmailCheckedFlag(Boolean.TRUE);
        insertUser.setEnabledFlag(Boolean.TRUE);
        insertUser.setActivedFlag(Boolean.TRUE);
        String hashPassword = lifeUser.getHashPassword();
        log.info("加密前->{}", hashPassword);
        hashPassword = passwordEncoder.encode(hashPassword);
        log.info("加密后->{}", hashPassword);
        insertUser.setHashPassword(hashPassword);
        insertUser.setEffectiveStartDate(LocalDateTime.now());
        //默认有效期至十年后
        insertUser.setEffectiveEndDate(LocalDateTime.now().plusYears(10));
        mapper.insert(insertUser);
        return insertUser.getUserId();
    }

    private String getUserName() {
        String username = RandomUtil.randomStringUpper(16);
        Long selectUserNameCount = mapper.selectCount(Wrappers.lambdaQuery(new LifeUser()).eq(LifeUser::getUsername, username));
        if (selectUserNameCount > 0) {
            return getUserName();
        }
        return username;
    }

    @Override
    public Boolean validateEmailExist(LifeUserDTO lifeUser) {
        Assert.notBlank(lifeUser.getEmail(), "邮箱不能为空.");
        ValidUtil.validateAndThrow(lifeUser, ValidGroup.class);
        return mapper.selectCount(Wrappers.lambdaQuery(new LifeUser()).eq(LifeUser::getEmail, lifeUser.getEmail())) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(LifeUserDTO lifeUser) {
        ValidUtil.validateAndThrow(lifeUser, LifeUserDTO.ResetPwdGroup.class);
        Assert.isTrue(lifeUser.getHashPassword().equals(lifeUser.getValidHashPassword()), "两次输入密码不一致,请重新输入.");

        boolean verifiedExpirationDate = RedisCaptchaValidator.verifyExpirationDate(redisUtil, lifeUser.getCaptchaUuid(), lifeUser.getCaptchaCode());
        Assert.isTrue(verifiedExpirationDate, "验证码已过期, 请重新获取.");
        boolean verify = RedisCaptchaValidator.verify(redisUtil, lifeUser.getCaptchaUuid(), lifeUser.getCaptchaCode());
        Assert.isTrue(verify, "验证码不正确,请重新输入.");

        //此处是验证通过, 将删除缓存中的验证码和邮箱校验逻辑
        RedisKeyValid.delEmailRepeatSendCode(redisUtil, lifeUser.getEmail());
        RedisKeyValid.delEmailRegSendCode(redisUtil, lifeUser.getCaptchaUuid());

        LambdaQueryWrapper<LifeUser> queryEmailWrapper = Wrappers.lambdaQuery(new LifeUser())
                .eq(LifeUser::getEmail, lifeUser.getEmail());
        LifeUser lifeUserOne = mapper.selectOne(queryEmailWrapper);
        Assert.notNull(lifeUserOne, "未找到所需要重置的邮箱信息.");

        String oldHashPassword = lifeUser.getHashPassword();
        log.info("加密前->{}", oldHashPassword);
        String newHashPassword = passwordEncoder.encode(oldHashPassword);
        log.info("加密后->{}", newHashPassword);

        boolean matches = passwordEncoder.matches(oldHashPassword, newHashPassword);
        log.info("验证是否生成成功->{}", matches);

        LifeUser updateUser = new LifeUser();
        updateUser.setUserId(lifeUserOne.getUserId());
        updateUser.setHashPassword(newHashPassword);
        mapper.updateById(updateUser);

    }

}
