package com.ma.service.impl;

import com.ma.bean.Users;
import com.ma.bean.UsersExample;
import com.ma.mapper.UsersMapper;
import com.ma.service.LoginAndRegistService;
import com.ma.util.FastDFSClient;
import com.ma.util.FileUtils;
import com.ma.util.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by mh on 2019/1/5.
 */
@Service
public class LoginAndRegistServiceImpl implements LoginAndRegistService{
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

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
        String userId = UUID.randomUUID().toString().replaceAll("-", "");
        user.setId(userId);

        String qrCodePath = "D://temp//user" + userId + "qrcode.png";
        // muxin_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath, "machat_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);

        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setQrcode(qrCodeUrl);
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
