package com.ma.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by mh on 2019/1/5.
 */
public class WebSocketServerInitialzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 以下是用于支持http协议
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //心跳检测
        pipeline.addLast(new IdleStateHandler(8, 10, 12));
        pipeline.addLast(new HeartHandler());

        // 以下是支持httpWebsocket
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new ChatHandler());
    }

}
