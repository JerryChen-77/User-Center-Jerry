package com.jerry.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jerry.usercenter.model.domain.User;
import com.jerry.usercenter.model.domain.request.UserLoginRequest;
import com.jerry.usercenter.model.domain.request.UserRegisterRequest;
import com.jerry.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jerry.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.jerry.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * author jerry
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return userService.UserRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        log.info("当前登录用户信息{}",userLoginRequest);
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.UserLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        //模糊查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);

        return userList.stream()
                .map(user -> userService.safetyUser(user))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestBody Long id,HttpServletRequest request){

        if (!isAdmin(request)) {
            return false;
        }
        if(id == null||id<=0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否位管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //鉴权 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;

        if(user == null || user.getUserRole() != ADMIN_ROLE){
            return false;
        }
        return true;
    }

}
