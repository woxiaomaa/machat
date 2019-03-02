package com.ma.service.impl;

import com.ma.bean.ChatMsgExample;
import com.ma.enums.MsgSignFlagEnum;
import com.ma.mapper.ChatMsgMapper;
import com.ma.mapper.UsersMapperCustom;
import com.ma.netty.bean.ChatMsg;
import com.ma.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by mh on 2019/2/14.
 */
@Service("chatServiceImpl")
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private UsersMapperCustom usersMapperCustom;

    @Override
    @Transactional
    public String saveMsg(ChatMsg chatMsg) {
        com.ma.bean.ChatMsg chatMsgForSql = new com.ma.bean.ChatMsg();
        String chatMsgId = UUID.randomUUID().toString().replaceAll("-", "");
        chatMsgForSql.setId(chatMsgId);
        chatMsgForSql.setAcceptUserId(chatMsg.getReceiverId());
        chatMsgForSql.setSendUserId(chatMsg.getSenderId());
        chatMsgForSql.setCreateTime(new Date());
        chatMsgForSql.setSignFlag(MsgSignFlagEnum.unsign.getType());
        chatMsgForSql.setMsg(chatMsg.getMsg());
        chatMsgMapper.insert(chatMsgForSql);
        return chatMsgId;
    }

    @Override
    @Transactional
    public void updateMsgSigned(List<String> msgIdList) {
        usersMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    @Override
    public List<com.ma.bean.ChatMsg> getUnReadMsgList(String userId) {
        ChatMsgExample chatMsgExample = new ChatMsgExample();
        ChatMsgExample.Criteria criteria = chatMsgExample.createCriteria();
        criteria.andSignFlagEqualTo(MsgSignFlagEnum.unsign.getType());
        criteria.andAcceptUserIdEqualTo(userId);
        return chatMsgMapper.selectByExample(chatMsgExample);
    }
}
