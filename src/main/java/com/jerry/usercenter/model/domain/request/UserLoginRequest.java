package com.jerry.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -4543495111423034130L;

    private String userAccount;

    private String userPassword;
}

