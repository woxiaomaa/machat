package com.ma.controller;

import com.ma.bean.Users;

import com.ma.bean.vo.UsersVo;
import com.ma.enums.SearchFriendsStatusEnum;
import com.ma.service.FriendService;

import com.ma.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;

/**
 * Created by mh on 2019/2/13.
 */
@RestController
@RequestMapping("fri")
public class FriendController {
    @Autowired
    private FriendService friendService;


    /**
     *搜索好友
     */
    @PostMapping("/search")
    public Result search(String myUserId, String friendUsername) throws Exception {
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return Result.errorMsg("");
        }


        Integer status = friendService.preconditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            Users user = friendService.queryUserByUsername(friendUsername);
            UsersVo userVo = new UsersVo();
            BeanUtils.copyProperties(user, userVo);
            return Result.ok(userVo);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return Result.errorMsg(errorMsg);
        }
    }

    @PostMapping("/addFriendRequest")
    public Result addFriendRequest(String myUserId, String friendUsername)
            throws Exception {
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return Result.errorMsg("");
        }

        Integer status = friendService.preconditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            friendService.sendFriendRequest(myUserId, friendUsername);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return Result.errorMsg(errorMsg);
        }

        return Result.ok();
    }
}
