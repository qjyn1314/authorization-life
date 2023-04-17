package com.authorization.life.system.infra.mapper;

import com.authorization.life.system.infra.entity.Tenant;
import com.authorization.life.system.infra.vo.TenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 租户信息转换类
 * <p>
 * 参考:
 * <p>
 * https://www.mobaijun.com/posts/239252401.html
 * <p>
 * https://www.jianshu.com/p/f28e3a773eb1
 * <p>
 * 官方教程:
 * <p>
 * https://github.com/mapstruct/mapstruct-examples
 *
 * @author wangjunming
 * @date 2023/3/27 13:49
 */
@Mapper
public interface TenantConvert {

    /**
     * 获取该类自动生成的实现类的实例
     * 接口中的属性都是 public static final 的 方法都是public abstract的
     */
    TenantConvert instance = Mappers.getMapper(TenantConvert.class);

    /**
     * 这个方法就是用于实现对象属性复制的方法
     *
     * @param tenant 这个参数就是源对象，也就是需要被复制的对象
     * @return 返回的是目标对象，就是最终的结果对象
     * @Mapping 用来定义属性复制规则 source 指定源对象属性 target指定目标对象属性
     */
    @Mappings({})
    TenantVO toVO(Tenant tenant);

    @Mappings({})
    Tenant toEntity(TenantVO tenantVO);


}