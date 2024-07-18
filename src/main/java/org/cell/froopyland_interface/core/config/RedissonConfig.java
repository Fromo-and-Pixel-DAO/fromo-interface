package org.cell.froopyland_interface.core.config;


import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description redisson config
 *
 * @author yozora
 **/
@Log4j2
@Configuration
public class RedissonConfig {

    @Value("${spring.redisson.profiles}")
    private String profiles;

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws Exception {
        log.info("loading redisson config: {}", profiles);
        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource(profiles));
        config.setThreads(0);
        config.setTransportMode(TransportMode.NIO);
        config.setCodec(StringCodec.INSTANCE);
        return Redisson.create(config);
    }

}
