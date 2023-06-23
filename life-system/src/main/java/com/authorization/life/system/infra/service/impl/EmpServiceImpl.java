package com.authorization.life.system.infra.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.authorization.life.system.infra.dto.EmpDTO;
import com.authorization.life.system.infra.entity.Emp;
import com.authorization.life.system.infra.mapper.EmpMapper;
import com.authorization.life.system.infra.service.EmpService;
import com.authorization.life.system.infra.vo.EmpVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 员工表
 *
 * @author code@code.com
 * @date 2023-06-23 14:38:43
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSaveEmp(List<Emp> empList) {
        boolean saveFlag = false;
        for (int i = 0; i < empList.size(); i++) {
//            if (i == 123) {
//                HulunBuirException.build("要出错后看看是否能管理事务..");
//            }
            Emp emp = empList.get(i);
            String empNum = emp.getEmpNum();
            String randomNum = recursionInitEmpNum(emp, empNum);
            emp.setEmpNum(randomNum);
            empMapper.insert(emp);
            saveFlag = true;
        }
        return saveFlag;
    }

    private String recursionInitEmpNum(Emp emp, String empNum) {
        if (StrUtil.isNotBlank(empNum)) {
            return empNum;
        }
        empNum = randomUsername();
        //用户编码
        LambdaQueryWrapper<Emp> queryUserNameWrapper = Wrappers.query(new Emp().setTenantId(emp.getTenantId()).setEmpNum(empNum)).lambda();
        Long userNameCount = empMapper.selectCount(queryUserNameWrapper);
        if (userNameCount == 0) {
            return empNum;
        }
        return recursionInitEmpNum(emp, null);
    }

    /**
     * 当生成的字符串的第一位为0时，则重新生成。
     */
    private String randomUsername() {
        String userName = RandomUtil.randomNumbers(8);
        if (StrUtil.startWith(userName, "0")) {
            userName = randomUsername();
        }
        return userName;
    }

    @Override
    public PageInfo<Emp> page(EmpDTO empDto) {
        Assert.notNull(empDto.getTenantId(), "租户id不能为空. ");
        QueryWrapper<Emp> queryWrapper = Wrappers.query(new Emp().setTenantId(empDto.getTenantId()));
        queryWrapper.likeRight(StrUtil.isNotBlank(empDto.getEmpName()), Emp.FIELD_EMP_NAME, empDto.getEmpName());
        return PageHelper.startPage(empDto.getPageNum(), empDto.getPageSize())
                .doSelectPageInfo(() -> {
                    empMapper.selectList(queryWrapper);
                });
    }

    @Override
    public Boolean batchDel(List<Emp> empList) {
        boolean flag = false;
        for (Emp emp : empList) {
            QueryWrapper<Emp> queryWrapper = Wrappers.query(new Emp());
            queryWrapper.eq(Objects.nonNull(emp.getTenantId()), Emp.FIELD_TENANT_ID, emp.getTenantId());
            queryWrapper.likeLeft(Emp.FIELD_EMP_NAME, emp.getEmpName());
            List<Emp> selectList = empMapper.selectList(queryWrapper);
            for (Emp delEmp : selectList) {
                empMapper.deleteById(delEmp.getEmpId());
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public EmpVO view(EmpDTO empDto) {

        LambdaQueryWrapper<Emp> queryWrapper = Wrappers.query(new Emp()).lambda();
        queryWrapper.eq(Emp::getTenantId, empDto.getTenantId());
        queryWrapper.eq(Emp::getEmpName, empDto.getEmpName());

        List<Emp> selectList = empMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(selectList)) {
            return new EmpVO();
        }
        return Convert.convert(EmpVO.class, selectList.iterator().next());
    }

    @Override
    public EmpVO update(EmpDTO empDto) {
        QueryWrapper<Emp> queryWrapper = Wrappers.emptyWrapper();
        queryWrapper.eq(Objects.nonNull(empDto.getTenantId()), Emp.FIELD_TENANT_ID, empDto.getTenantId());
        queryWrapper.eq(Objects.nonNull(empDto.getEmpId()), Emp.FIELD_EMP_ID, empDto.getEmpId());
        Emp updateEmp = Convert.convert(Emp.class, empDto);
        empMapper.update(updateEmp, queryWrapper);
        return Convert.convert(EmpVO.class, updateEmp);
    }


}
