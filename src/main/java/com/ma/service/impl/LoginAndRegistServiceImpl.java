package com.ma.service.impl;

import com.ma.bean.Users;
import com.ma.bean.UsersExample;
import com.ma.mapper.UsersMapper;
import com.ma.service.LoginAndRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mh on 2019/1/5.
 */
@Service
public class LoginAndRegistServiceImpl implements LoginAndRegistService{
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public boolean ifUserExist(String username) {
        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria criteria = usersExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<Users> users = usersMapper.selectByExample(usersExample);
        return users.size() != 0;
    }

    @Override
    public Users getByUsername(String username) {
        return usersMapper.getByUsername(username);
    }

    @Override
    @Transactional
    public int saveUser(Users user) {
        user.setId("0000001");
        user.setQrcode("none");
        return usersMapper.insert(user);
    }

    @Override
    @Transactional
    public Users updateUser(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    private Users queryUserById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

}
