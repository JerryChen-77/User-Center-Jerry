package com.jerry.usercenter.service;

import com.jerry.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author Jerry Chen
* @description 针对表【user(table)】的数据库操作Service
* @createDate 2024-09-05 18:41:43
*/
public interface UserService extends IService<User> {
    /**
     * 用户登录态键
     */


    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 用户id
     */
    long UserRegister(String userAccount,String userPassword,String checkPassword);

    /**用户登录
     *
     * @param userAccount
     * @param userPassword
     * @return 脱敏后的用户信息
     */
    User UserLogin(String userAccount, String userPassword, HttpServletRequest request);

    User safetyUser(User user);
}
