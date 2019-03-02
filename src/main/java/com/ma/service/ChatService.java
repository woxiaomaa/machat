package com.ma.service;

import com.ma.netty.bean.ChatMsg;

import java.util.List;

/**
 * Created by mh on 2019/2/14.
 */
public interface ChatService {
    String saveMsg(ChatMsg chatMsg);
    void updateMsgSigned(List<String> msgIdList);
    public List<com.ma.bean.ChatMsg> getUnReadMsgList(String userId);
}
