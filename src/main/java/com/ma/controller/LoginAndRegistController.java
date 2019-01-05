package com.ma.controller;

import com.ma.bean.Users;
import com.ma.bean.vo.UsersVo;
import com.ma.service.LoginAndRegistService;
import com.ma.util.MD5Utils;
import com.ma.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mh on 2019/1/5.
 */
@RestController
@RequestMapping("lar")
public class LoginAndRegistController {
    @Autowired
    private LoginAndRegistService loginAndRegistService;

    @PostMapping("/loginAndRegist")
    public Result login(@RequestBody Users user) throws Exception {
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) return Result.errorMsg("账户名或密码不能为空");
        Users uResult = null;
        UsersVo usersVo = new UsersVo();
        if(loginAndRegistService.ifUserExist(user.getUsername())){
            uResult = loginAndRegistService.getByUsername(user.getUsername());
            if(uResult != null){
                //账号密码验证成功
                BeanUtils.copyProperties(uResult,usersVo);
            }else{
                return Result.errorMsg("用户名或密码错误");
            }
        }else{
            //注册
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFaceImage("");
            user.setFaceImageBig("");
            loginAndRegistService.saveUser(user);
            BeanUtils.copyProperties(user,usersVo);
        }
        return Result.ok(usersVo);
    }
}
