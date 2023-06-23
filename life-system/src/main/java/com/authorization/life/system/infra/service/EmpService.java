package com.authorization.life.system.infra.service;

import com.authorization.life.system.infra.dto.EmpDTO;
import com.authorization.life.system.infra.entity.Emp;
import com.authorization.life.system.infra.vo.EmpVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 员工表
 *
 * @author code@code.com
 * @date 2023-06-23 14:38:43
 */
public interface EmpService {


    Boolean batchSaveEmp(List<Emp> empList);


    PageInfo<Emp> page(EmpDTO empDto);


    Boolean batchDel(List<Emp> empList);


    EmpVO view(EmpDTO empDto);


    EmpVO update(EmpDTO empDto);

}
