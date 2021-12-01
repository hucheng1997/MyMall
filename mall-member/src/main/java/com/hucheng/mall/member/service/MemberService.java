package com.hucheng.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.member.entity.MemberEntity;
import com.hucheng.mall.member.vo.MemberUserLoginVo;
import com.hucheng.mall.member.vo.MemberUserRegisterVo;
import com.hucheng.mall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:53:44
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberUserRegisterVo vo);

    MemberEntity login(MemberUserLoginVo vo);

    /**
     * 社交用户的登录
     */
    MemberEntity login(SocialUser socialUser) throws Exception;
}

