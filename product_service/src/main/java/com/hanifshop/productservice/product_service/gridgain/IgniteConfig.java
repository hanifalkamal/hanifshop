package com.hanifshop.productservice.product_service.gridgain;

import com.mysql.cj.log.Slf4JLogger;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.PersistentStoreConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author Hanif al kamal 16/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Configuration
public class IgniteConfig {
//    @Bean(name = "ignite")
    public Ignite ignite() {
        IgniteConfiguration igniteConfig = new IgniteConfiguration();
        igniteConfig.setLifecycleBeans(new IgniteLifeCycle());
        igniteConfig.setClientMode(true);
        igniteConfig.setPeerClassLoadingEnabled(true);
        igniteConfig.setPersistentStoreConfiguration(new PersistentStoreConfiguration());
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("103.82.242.61:10800"));
        igniteConfig.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        return Ignition.start(igniteConfig);
//
    }

}
