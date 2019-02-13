package com.ma.controller;

import com.ma.bean.Users;

import com.ma.bean.vo.MyFriendsVo;
import com.ma.bean.vo.UsersVo;
import com.ma.enums.OperatorFriendRequestTypeEnum;
import com.ma.enums.SearchFriendsStatusEnum;
import com.ma.service.FriendService;

import com.ma.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by mh on 2019/2/13.
 */
@RestController
@RequestMapping("fri")
public class FriendController {
    @Autowired
    private FriendService friendService;


    /**
     * 搜索好友
     * @param myUserId
     * @param friendUsername
     * @return
     * @throws Exception
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

    /**
     * 发送添加好友请求
     * @param myUserId
     * @param friendUsername
     * @return
     * @throws Exception
     */
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

    /**
     * 查询用户接受到的朋友申请
     * @param userId
     * @return
     */
    @PostMapping("/queryFriendRequests")
    public Result queryFriendRequests(String userId) {
        if (StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }
        return Result.ok(friendService.queryFriendRequestList(userId));
    }


    /**
     *处理好友请求（通过或拒绝
     * @param acceptUserId
     * @param sendUserId
     * @param operType
     * @return
     */
    @PostMapping("/operFriendRequest")
    public Result operFriendRequest(String acceptUserId, String sendUserId,
                                             Integer operType) {

        if (StringUtils.isBlank(acceptUserId)
                || StringUtils.isBlank(sendUserId)
                || operType == null) {
            return Result.errorMsg("");
        }

        if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
            return Result.errorMsg("");
        }

        if (operType == OperatorFriendRequestTypeEnum.IGNORE.type) {
            friendService.deleteFriendRequest(sendUserId, acceptUserId);
        } else if (operType == OperatorFriendRequestTypeEnum.PASS.type) {
            friendService.passFriendRequest(sendUserId, acceptUserId);
        }

        // 数据库查询好友列表
        List<MyFriendsVo> myFirends = friendService.queryMyFriends(acceptUserId);

        return Result.ok(myFirends);
    }

    /**
     * 根据id获取好友列表
     * @param userId
     * @return
     */
    @PostMapping("/myFriends")
    public Result myFriends(String userId) {
        if (StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }
        List<MyFriendsVo> myFirends = friendService.queryMyFriends(userId);
        return Result.ok(myFirends);
    }
}
