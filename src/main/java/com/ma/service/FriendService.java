package com.ma.service;

import com.ma.bean.Users;

/**
 * Created by mh on 2019/2/13.
 */
public interface FriendService {
    /**
     * 搜索好友
     * @param myUserId
     * @param friendUsername
     * @return
     */
    public Integer preconditionSearchFriends(String myUserId, String friendUsername);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public Users queryUserByUsername(String username);


    public void sendFriendRequest(String myUserId, String friendUsername);
}
