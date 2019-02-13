package com.ma.service.impl;

import com.ma.bean.*;
import com.ma.enums.SearchFriendsStatusEnum;
import com.ma.mapper.FriendsRequestMapper;
import com.ma.mapper.MyFriendsMapper;
import com.ma.mapper.UsersMapper;
import com.ma.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by mh on 2019/2/13.
 */
@Service
public class FriendServiceImpl implements FriendService{
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;


    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        Users user = usersMapper.getByUsername(friendUsername);


        // 1. 搜索的用户如果不存在，返回[无此用户]
        if (user == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }

        // 2. 搜索账号是你自己，返回[不能添加自己]
        if (user.getId().equals(myUserId)) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }

        // 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        MyFriendsExample myFriendsExample = new MyFriendsExample();
        MyFriendsExample.Criteria criteria = myFriendsExample.createCriteria();
        criteria.andMyUserIdEqualTo(myUserId);
        criteria.andFriendUserIdEqualTo(user.getId());
        List<MyFriends> list = myFriendsMapper.selectByExample(myFriendsExample);

        if (list.size() != 0) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Override
    public Users queryUserByUsername(String username) {
        return usersMapper.getByUsername(username);
    }

    @Override
    @Transactional
    public void sendFriendRequest(String myUserId, String friendUsername) {
        Users friend = usersMapper.getByUsername(friendUsername);
        new FriendsRequestExample();
        FriendsRequestExample friendsRequestExample = new FriendsRequestExample();
        FriendsRequestExample.Criteria criteria = friendsRequestExample.createCriteria();
        criteria.andSendUserIdEqualTo(myUserId);
        criteria.andAcceptUserIdEqualTo(friend.getId());
        List<FriendsRequest> list = friendsRequestMapper.selectByExample(friendsRequestExample);
        if (list.size() == 0) {
            String requestId = UUID.randomUUID().toString().replaceAll("-", "");
            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDateTime(new Date());
            friendsRequestMapper.insert(request);

        }
    }
}
