package com.authorization.life.lov.start.lov.service;


import com.authorization.life.lov.start.lov.entity.LovDetail;
import com.authorization.life.lov.start.lov.entity.LovValueDetail;

import java.util.List;

public interface LovService {

    LovDetail selectLov(Long tenantId, String lovCode);

    List<LovValueDetail> selectLovValue(Long tenantId, String lovCode);

}
