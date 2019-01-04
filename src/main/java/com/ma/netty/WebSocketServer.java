package com.ma.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by mh on 2019/1/4.
 */
@Component
@Slf4j
public class WebSocketServer {
    //单例模式
    private static class SingleWebSocketServer{
        static final WebSocketServer wsServer = new WebSocketServer();
    }

    public static WebSocketServer getInstance(){
        return SingleWebSocketServer.wsServer;
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private WebSocketServer(){
        bossGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(bossGroup,subGroup).channel(NioServerSocketChannel.class).childHandler(new WebSocketServerInitialzer());
    }

    public void start(){
        server.bind(9090);
        log.info("netty启动...");
    }
}
