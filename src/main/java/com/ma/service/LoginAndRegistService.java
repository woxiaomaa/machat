package com.ma.service;

import com.ma.bean.Users;

/**
 * Created by mh on 2019/1/5.
 */
public interface LoginAndRegistService {
    Users ifUserExist(String username);
    Users getByUsername(String username);
    int saveUser(Users user);
    Users updateUser(Users user);
}
