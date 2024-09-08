package com.jerry.usercenter.service;
import java.util.Date;

import com.jerry.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;
    @Test
    public void testAddUser(){
        User user = new User();

        user.setUsername("dogJerry");
        user.setUserAccount("123");
        user.setAvatarUrl("http://34565432r");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        user.setUserStatus(0);

        boolean res = userService.save(user);
        System.out.println(user.getId());
        assertTrue(res);
    }

    @Test
    void userRegister() {
        String userAccount = "jerry";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userService.UserRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
        userAccount = "yu";
        result = userService.UserRegister(userAccount,userPassword,checkPassword);
        assertEquals(-1,result);
        userAccount = "jerry";
        userPassword = "123456";
        result = userService.UserRegister(userAccount,userPassword,checkPassword);
        assertEquals(-1,result);
        userAccount ="je   rry";
        userPassword ="12345678";
        result = userService.UserRegister(userAccount,userPassword,checkPassword);
        assertEquals(-1,result);
        checkPassword = "123456789";
        result = userService.UserRegister(userAccount,userPassword,checkPassword);
        assertEquals(-1,result);
        userAccount = "dogJerry";
        checkPassword = "12345678";
        result = userService.UserRegister(userAccount,userPassword,checkPassword);
        assertEquals(-1,result);
        userAccount = "jerry";
        result = userService.UserRegister(userAccount,userPassword,checkPassword);
        assertTrue(result>0);

    }
}