package com.authorization.life.auth.infra.security.service;

import cn.hutool.core.lang.Assert;
import com.authorization.core.security.entity.UserDetail;
import com.authorization.core.security.UserDetailService;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.service.UserGroupService;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.entity.LifeUser;
import com.authorization.life.auth.infra.entity.UserGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
    public LifeUser loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("登录时查询用户[{}]信息。", username);
        LifeUser lifeUser = userService.selectByUsername(username);
        Assert.notNull(lifeUser, () -> new UsernameNotFoundException("user [" + username + "] not found"));
        List<UserGroup> userGroups = groupService.selectByUserId(lifeUser.getUserId());
        lifeUser.setUserGroups(userGroups.stream().map(UserGroup::getUserGroupCode).collect(Collectors.toSet()));
        return lifeUser;
    }

    @Override
    public UserDetail createUserDetailByUser(UserDetails userDetails) {
        LifeUser lifeUser = (LifeUser)userDetails;
        UserDetail userDetail = createUserDetail(lifeUser);

        //此处将获取到当前已登录用户所关联的员工、或者关联的当前登录的授权信息中所需要的其他信息，作为扩展。

        return userDetail;
    }
    private UserDetail createUserDetail(LifeUser lifeUser){
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(lifeUser.getUserId());
        userDetail.setUsername(lifeUser.getUsername());
        userDetail.setUserEmail(lifeUser.getEmail());
        userDetail.setUserGender(lifeUser.getGender());
        userDetail.setUserActivedFlag(lifeUser.getActivedFlag());
        userDetail.setUserPhone(lifeUser.getPhone());
        userDetail.setUserEnabledFlag(lifeUser.getEnabledFlag());
        userDetail.setUserLockedFlag(lifeUser.getLockedFlag());
        userDetail.setRealName(lifeUser.getRealName());
        userDetail.setLanguage(lifeUser.getLang());
        userDetail.setUserGroups(lifeUser.getUserGroups());
        return userDetail;
    }


    public UserDetail createUserDetailByClientId(String clientId) {
        OauthClientVO oauthClient = oauthClientService.selectClientByClientId(clientId);
        UserDetail userDetail = new UserDetail();
        userDetail.setUsername(oauthClient.getClientId());
        userDetail.setTenantId(oauthClient.getTenantId());
        return userDetail;
    }
}
