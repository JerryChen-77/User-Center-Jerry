package com.jerry.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerry.usercenter.model.domain.User;
import com.jerry.usercenter.service.UserService;
import com.jerry.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jerry.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Jerry Chen
* @description 针对表【user(table)】的数据库操作Service实现
* @createDate 2024-09-05 18:41:43
 * 用户实现类
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    public UserMapper userMapper;
    /**
     * 盐值
     */
    private final String SALT = "jerry";


    @Override
    public long UserRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        if(userAccount.length()<4)
            return -1;
        if(userPassword.length()<8||checkPassword.length()<8){
            return -1;
        }
        // 账户无特殊字符
        String validPattern ="[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            return -1;
        }

        // 密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        // 账户不重复
        QueryWrapper <User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            return -1;
        }


        // 加密
        String new_pwd = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        // 向数据库加入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(new_pwd);
        boolean save = this.save(user);
        if(!save){
            return -1;
        }
        return user.getId();
    }

    /**用户登录
     *
     * @param userAccount
     * @param userPassword
     * @return 脱敏后的用户信息
     */
    @Override
    public User UserLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if(userAccount.length()<4)
            return null;
        if(userPassword.length()<8){
            return null;
        }
        // 账户无特殊字符
        String validPattern ="[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            return null;
        }

        // 加密
        String new_pwd = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper <User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",new_pwd);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if(user == null){
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }
        //脱敏
        User safe_user = safetyUser(user);
        //记录用户登录态
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE,safe_user);
        return safe_user;
    }


    @Override
    public User safetyUser(User user){
        User safetyuser = new User();
        safetyuser.setId(user.getId());
        safetyuser.setUsername(user.getUsername());
        safetyuser.setUserAccount(user.getUserAccount());
        safetyuser.setAvatarUrl(user.getAvatarUrl());
        safetyuser.setGender(user.getGender());
        safetyuser.setPhone(user.getPhone());
        safetyuser.setEmail(user.getEmail());
        safetyuser.setUserStatus(user.getUserStatus());
        safetyuser.setCreateTime(user.getCreateTime());
        safetyuser.setUserRole(user.getUserRole());
        return safetyuser;
    }
}




