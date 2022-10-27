package com.yzy.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 登录相关查询
 */
@Mapper
public interface LoginDao {
    /**
     * 通过用户名查询对应的密码
     *
     * @param userName 用户名
     * @return 返回用户名对应的密码
     */
    @Select("select user_password from user where user_name = #{userName}")
    public String getUserPassword(String userName);
}
