package com.yzy.controller;

import com.yzy.entity.User;
import com.yzy.service.LoginService;
import com.yzy.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 登录页面控制类
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 响应登录请求，密码正确则返回生成的token，不正确则返回错误信息
     *
     * @param user    用户信息
     * @param session 回话
     * @return 返回登录验证结果
     */
    @PostMapping("/login")
    public JsonResult login(@RequestBody User user, HttpSession session) {
        String result = loginService.login(user);
        String token = TokenUtil.sign(user);
        session.setAttribute("token", token);
        if (result.equals("用户名错误或不存在！") || result.equals("密码输入错误！"))
            return new JsonResult(Code.LOGIN_ERR, null, result);
        else
            return new JsonResult(Code.LOGIN_OK, token, result);
    }

}
