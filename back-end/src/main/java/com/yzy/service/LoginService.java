package com.yzy.service;

import com.yzy.entity.User;

/**
 * 处理登录业务
 */
public interface LoginService {
    /**
     * 登录功能
     *
     * @param user 用户信息
     * @return 根据用户输入的用户名和密码判断输入是否正确，正确则登录成功，不正确则返回错误原因（均为字符串）
     */
    public String login(User user);
}
