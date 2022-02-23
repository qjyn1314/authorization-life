package com.authserver.life.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *    查询用户信息
 * </p>
 *
 * @author wangjunming
 * @since 2022/2/23 16:40
 */
@Service
public class AuthUserServiceImpl implements UserDetailService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
