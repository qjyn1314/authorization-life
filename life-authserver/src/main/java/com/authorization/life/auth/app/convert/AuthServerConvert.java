package com.authorization.life.auth.app.convert;

import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.entity.OauthClient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2025-06-10 12:37
 */
@Mapper
public interface AuthServerConvert {

    AuthServerConvert INSTANCE = Mappers.getMapper(AuthServerConvert.class);

    OauthClientVO toOauthClientVO(OauthClient oauthClient);

}
