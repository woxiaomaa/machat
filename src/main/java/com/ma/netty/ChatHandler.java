package com.ma.netty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.ma.SpringUtil;
import com.ma.enums.MsgActionEnum;
import com.ma.netty.bean.ChatMsg;
import com.ma.netty.bean.DataContent;
import com.ma.service.ChatService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by mh on 2019/1/5.
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {
        String content = msg.text();
        Channel currentChannel = ctx.channel();

        Gson gson = new Gson();
        DataContent dataContent = gson.fromJson(content, DataContent.class);
        Integer action = dataContent.getAction();

        // 2. 判断消息类型，根据不同的类型来处理不同的业务
        if (action == MsgActionEnum.CONNECT.type) {
            // 	2.1  当websocket 第一次open的时候，初始化channel，把用的channel和userid关联起来
            //app当退出时关闭channel，重新代开app再次将channel和userid关联起来，但是此时channel与原来的channel不同了
            String senderId = dataContent.getChatMsg().getSenderId();
            UserChannelRelation.put(senderId, currentChannel);

            // 测试
            for (Channel c : clients) {
                System.out.println(c.id().asLongText());
            }
            UserChannelRelation.output();
        } else if (action == MsgActionEnum.CHAT.type) {
            //  2.2  聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
            ChatMsg chatMsg = dataContent.getChatMsg();
            String msgText = chatMsg.getMsg();
            String receiverId = chatMsg.getReceiverId();
            String senderId = chatMsg.getSenderId();

            // 保存消息到数据库，并且标记为 未签收
            ChatService chatService = (ChatService) SpringUtil.getBean("chatServiceImpl");
            String msgId = chatService.saveMsg(chatMsg);
            chatMsg.setMsgId(msgId);

            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);

            // 发送消息
            // 从全局用户Channel关系中获取接受方的channel
            Channel receiverChannel = UserChannelRelation.get(receiverId);
            if (receiverChannel == null) {
                // TODO channel为空代表用户离线，推送消息（JPush，个推，小米推送）
                log.info("receiverChannel为空");
            } else {
                // 当receiverChannel不为空的时候，从ChannelGroup去查找对应的channel是否存在
                Channel findChannel = clients.find(receiverChannel.id());
                if (findChannel != null) {
                    // 用户在线
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(dataContentMsg)));
                } else {
                    // 用户离线 TODO 推送消息
                    log.info("channel不存在");
                }
            }

        } else if (action == MsgActionEnum.SIGNED.type) {
            //  2.3  签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
            ChatService chatService = (ChatService)SpringUtil.getBean("chatServiceImpl");
            // 扩展字段在signed类型的消息中，代表需要去签收的消息id，逗号间隔
            String msgIdsStr = dataContent.getExtend();
            String msgIds[] = msgIdsStr.split(",");

            List<String> msgIdList = new ArrayList<>();
            for (String mid : msgIds) {
                if (StringUtils.isNotBlank(mid)) {
                    msgIdList.add(mid);
                }
            }

            System.out.println(msgIdList.toString());

            if (!msgIdList.isEmpty() && msgIdList.size() > 0) {
                // 批量签收
                chatService.updateMsgSigned(msgIdList);
            }

        } else if (action == MsgActionEnum.KEEPALIVE.type) {
            //  2.4  心跳类型的消息
            //System.out.println("收到来自channel为[" + currentChannel + "]的心跳包...");
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
		clients.remove(ctx.channel());
//      System.out.println("客户端断开，channle长id为：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{}连接中断",ctx.channel().id().asLongText());
        ctx.channel().close();
        clients.remove(ctx.channel());
    }
}
