package com.authorization.life.auth.infra.mapper;

import java.util.List;

import com.authorization.life.auth.entity.OauthClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * oauth客户端表 持久
 *
 * @author code@code.com 2022-02-21 20:21:01
 */
@Repository
public interface OauthClientMapper extends BaseMapper<OauthClient> {

    List<OauthClient> page(OauthClient oauthclient);

}
