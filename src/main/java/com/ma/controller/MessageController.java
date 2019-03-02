package com.ma.controller;

import com.ma.bean.ChatMsg;
import com.ma.service.ChatService;
import com.ma.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mh on 2019/2/15.
 */
@RestController
@RequestMapping("chat")
public class MessageController {
    @Autowired
    private ChatService chatService;

    /**
     * 获取未签收消息
     * @param acceptUserId
     * @return
     */
    @PostMapping("/getUnReadMsgList")
    public Result getUnReadMsgList(String acceptUserId) {
        if (StringUtils.isBlank(acceptUserId)) {
            return Result.errorMsg("");
        }
        List<ChatMsg> unreadMsgList = chatService.getUnReadMsgList(acceptUserId);
        return Result.ok(unreadMsgList);
    }
}
