package com.ma;

import com.ma.netty.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by mh on 2019/1/5.
 */
@Slf4j
@Component
public class NettyStarter implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            try {
                WebSocketServer.getInstance().start();
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }
}
