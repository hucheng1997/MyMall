package com.hucheng.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:53:44
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

