package com.hanifshop.productservice.product_service;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
@EnableIgniteRepositories
public class ProductServiceApplication {


    public static void main(String[] args) {
//        SpringApplication.run(ProductServiceApplication.class, args);

        IgniteConfiguration igniteConfig = new IgniteConfiguration();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("103.82.242.61:10800")); // Ganti dengan alamat server Ignite Anda.
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setIpFinder(ipFinder);
        igniteConfig.setDiscoverySpi(discoverySpi);
//        igniteConfig.setLifecycleBeans(new IgniteLifeCycle());
//        igniteConfig.setGridLogger(new Slf4JLogger());
        igniteConfig.setClientMode(true);
        Ignition.start(igniteConfig);
    }

}
