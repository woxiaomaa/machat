package com.ma.controller;

import com.ma.bean.Users;
import com.ma.bean.bo.UsersBo;
import com.ma.bean.vo.UsersVo;
import com.ma.service.LoginAndRegistService;
import com.ma.util.FastDFSClient;
import com.ma.util.FileUtils;
import com.ma.util.MD5Utils;
import com.ma.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by mh on 2019/1/5.
 */
@RestController
@RequestMapping("lar")
public class LoginAndRegistController {
    @Autowired
    private LoginAndRegistService loginAndRegistService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @PostMapping("/loginAndRegist")
    public Result login(@RequestBody Users user) throws Exception {
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) return Result.errorMsg("账户名或密码不能为空");
        UsersVo usersVo = new UsersVo();
        Users u = loginAndRegistService.ifUserExist(user.getUsername());
        if(u != null){
            if(u.getPassword().equals(MD5Utils.getMD5Str(user.getPassword()))){
                //账号密码验证成功
                BeanUtils.copyProperties(u,usersVo);
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

    @PostMapping("/uploadFaceBase64")
    public Result uploadFaceBase64 (@RequestBody UsersBo usersBo) throws Exception {
        // 获取前端传过来的base64字符串, 然后转换为文件对象再上传
        String base64Data = usersBo.getFaceData();
        String userFacePath = "D:\\temp\\" + usersBo.getUserId() + "userface64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);

        // 上传文件到fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        //System.out.println(url);

        //大图		"xxxxx.png"
        //小图		"xxxxx_80x80.png

        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        // 更细用户头像
        Users user = new Users();
        user.setId(usersBo.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);
        Users result = loginAndRegistService.updateUser(user);

        return Result.ok(result);
    }


    @PostMapping("/setNickname")
    public Result setNickname(@RequestBody UsersBo usersBo) throws Exception {

        Users user = new Users();
        user.setId(usersBo.getUserId());
        user.setNickname(usersBo.getNickname());

        Users result = loginAndRegistService.updateUser(user);

        return Result.ok(result);
    }
}
