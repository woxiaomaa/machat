package com.ma.service;

import com.ma.bean.Users;
import com.ma.bean.vo.FriendRequestVo;

import java.util.List;

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

    /**
     * 添加好友请求记录
     * @param myUserId
     * @param friendUsername
     */
    public void sendFriendRequest(String myUserId, String friendUsername);

    /**
     * 查询好友请求
     * @param acceptUserId
     * @return
     */
    public List<FriendRequestVo> queryFriendRequestList(String acceptUserId);

    /**
     * 删除好友请求
     * @param sendUserId
     * @param acceptUserId
     */
    public void deleteFriendRequest(String sendUserId, String acceptUserId);

    /**
     * 通过好友请求，保存好友，逆向保存好友，删除好友请求记录
     * @param sendUserId
     * @param acceptUserId
     */
    public void passFriendRequest(String sendUserId, String acceptUserId);
}
