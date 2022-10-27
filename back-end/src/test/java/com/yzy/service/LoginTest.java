package com.yzy.service;

import com.yzy.dao.LoginDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginTest {
    @Autowired
    private LoginDao loginDao;

    @Test
    public void loginTest(){
        if (loginDao.getUserPassword("admiffn") == null)
            System.out.println("ddd");
        System.out.println(loginDao.getUserPassword("admiffn"));
    }
}
