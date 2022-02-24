package com.authserver.life.security.service;

import com.authserver.life.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 查询用户信息
 */
@Service
public class AuthUserServiceImpl implements UserDetailsService {

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {


        return null;
    }

}
