package com.ma.mapper;

import java.util.List;

import com.ma.bean.vo.FriendRequestVo;
import com.ma.bean.vo.MyFriendsVo;

public interface UsersMapperCustom{
	
	public List<FriendRequestVo> queryFriendRequestList(String acceptUserId);
	
	public List<MyFriendsVo> queryMyFriends(String userId);
	
	public void batchUpdateMsgSigned(List<String> msgIdList);
	
}