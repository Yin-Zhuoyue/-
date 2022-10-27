package com.yzy.service.impl;

import com.yzy.dao.LoginDao;
import com.yzy.entity.User;
import com.yzy.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;

    @Override
    public String login(User user) {
        String userName = user.getUser_name();
        if (loginDao.getUserPassword(userName) == null)
            return "用户名错误或不存在！";
        else if (loginDao.getUserPassword(userName).equals(user.getUser_password()))
            return "登录成功！";
        else
            return "密码输入错误！";
    }
}
