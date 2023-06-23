package com.authorization.life.system.infra.mapper;

import java.util.List;

import com.authorization.life.system.infra.entity.Emp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 员工表 持久
 *
 * @author code@code.com 2023-06-23 14:38:43
 */
@Repository
public interface EmpMapper extends BaseMapper<Emp> {

    List<Emp> page(Emp lemdEmp);

}
