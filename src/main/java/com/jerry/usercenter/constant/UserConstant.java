package com.jerry.usercenter.constant;

/**
 * 用户常量类
 */

public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 权限
     * 0 - 普通用户
     * 1 - 管理员
     */
    int DEFAULT_ROLE = 0;
    int ADMIN_ROLE = 1;
}
