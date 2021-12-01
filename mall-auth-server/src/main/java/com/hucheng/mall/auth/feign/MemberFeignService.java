package com.hucheng.mall.auth.feign;

import com.hucheng.common.utils.R;
import com.hucheng.common.vo.UserLoginVo;
import com.hucheng.mall.auth.vo.SocialUser;
import com.hucheng.mall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author MrHu
 */
@FeignClient("mall-member")
public interface MemberFeignService {

    @PostMapping(value = "/member/member/register")
    R register(@RequestBody UserRegisterVo vo);

    @PostMapping(value = "/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping(value = "/member/member/oauth2/login")
    R oauthLogin(SocialUser socialUser);

}
