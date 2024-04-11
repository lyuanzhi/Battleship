package edu.duke.yl954.battleship;

import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(8082);
        config.setOrigin("*");

        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }
}
