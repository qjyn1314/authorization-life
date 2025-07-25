package com.authorization.life.security.start;

import com.authorization.utils.security.UserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailService extends UserDetailsService {

    UserDetail createUserDetailByUser(UserDetails userDetails);

}
