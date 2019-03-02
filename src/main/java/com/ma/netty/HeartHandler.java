package com.ma.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳检测
 * Created by mh on 2019/2/15.
 */
@Slf4j
public class HeartHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE) {
            } else if (event.state() == IdleState.WRITER_IDLE) {
            } else if (event.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                channel.close();
            }
        }
    }
}
