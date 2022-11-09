package com.authorization.life.security.service;

import cn.hutool.core.lang.Assert;
import com.authorization.core.entity.UserDetail;
import com.authorization.core.security.UserDetailService;
import com.authorization.life.entity.OauthClient;
import com.authorization.life.entity.User;
import com.authorization.life.entity.UserGroup;
import com.authorization.life.service.OauthClientService;
import com.authorization.life.service.UserGroupService;
import com.authorization.life.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
public class SecurityAuthUserService implements UserDetailService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService groupService;
    @Autowired
    private OauthClientService oauthClientService;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("登录时查询用户[{}]信息。", username);
        User user = userService.selectByUsername(username);
        Assert.notNull(user, () -> new UsernameNotFoundException("user [" + username + "] not found"));
        List<UserGroup> userGroups = groupService.selectByUserId(user.getUserId());
        user.setUserGroups(userGroups.stream().map(UserGroup::getUserGroupCode).collect(Collectors.toSet()));
        return user;
    }

    @Override
    public UserDetail createUserDetailByUser(UserDetails userDetails) {
        User user = (User)userDetails;
        UserDetail userDetail = createUserDetail(user);

        //此处将获取到当前已登录用户所关联的员工、或者关联的当前登录的授权信息中所需要的其他信息，作为扩展。

        return userDetail;
    }
    private UserDetail createUserDetail(User user){
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(user.getUserId());
        userDetail.setUsername(user.getUsername());
        userDetail.setUserEmail(user.getEmail());
        userDetail.setUserGender(user.getGender());
        userDetail.setUserActivedFlag(user.getActivedFlag());
        userDetail.setUserPhone(user.getPhone());
        userDetail.setUserEnabledFlag(user.getEnabledFlag());
        userDetail.setUserLockedFlag(user.getLockedFlag());
        userDetail.setRealName(user.getRealName());
        userDetail.setLanguage(user.getLang());
        userDetail.setUserGroups(user.getUserGroups());
        return userDetail;
    }


    public UserDetail createUserDetailByClientId(String clientId) {
        OauthClient oauthClient = oauthClientService.selectClientByClientId(clientId);
        UserDetail userDetail = new UserDetail();
        userDetail.setUsername(oauthClient.getClientId());
        userDetail.setTenantId(oauthClient.getTenantId());
        return userDetail;
    }
}
