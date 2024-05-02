package edu.duke.yl954.battleship;

import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(8086);
        config.setOrigin("*");
        config.setKeyStorePassword("pt3ck07bx45");
        config.setKeyStore(resourceLoader.getResource("classpath:lyuanzhi.com.jks").getInputStream());

        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }
}
