package com.authorization.life.security.service;

import cn.hutool.core.lang.Assert;
import com.authorization.life.entity.User;
import com.authorization.life.entity.UserGroup;
import com.authorization.life.service.UserGroupService;
import com.authorization.life.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询用户信息
 */
@Slf4j
@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService groupService;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("登录时查询用户[{}]信息。", username);
        User user = userService.selectByUsername(username);
        Assert.notNull(user, () -> new UsernameNotFoundException("user [" + username + "] not found"));
        List<UserGroup> userGroups = groupService.selectByUserId(user.getUserId());
        user.setUserGroups(userGroups.stream().map(UserGroup::getUserGroupCode).collect(Collectors.toSet()));
        return user;
    }

}
