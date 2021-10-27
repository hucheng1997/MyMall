package com.hucheng.mall.member.dao;

import com.hucheng.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:53:44
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
