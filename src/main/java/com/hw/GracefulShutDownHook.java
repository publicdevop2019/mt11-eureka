package com.hw;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;

@Configuration
@Slf4j
public class GracefulShutDownHook {
    @PreDestroy
    public void onExit() {
        log.info("Closing application..");
        LogManager.shutdown();
    }

    @Autowired
    InetUtils inetUtils;

    @Bean
    public void eurekaInstanceConfig() {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Iterator<NetworkInterface> networkInterfaceIterator = networkInterfaces.asIterator();
            networkInterfaceIterator.forEachRemaining(e -> {
                e.getInterfaceAddresses().stream().forEach(el -> {
                    log.info("NetworkInterface.getNetworkInterfaces " + el.getAddress().getHostAddress());
                });
            });
        } catch (SocketException e) {
            e.printStackTrace();
        }
        log.info("getHostAddress " + inetUtils.findFirstNonLoopbackAddress().getHostAddress());
        log.info("getIpAddress " + inetUtils.findFirstNonLoopbackHostInfo().getIpAddress());
        log.info("getHostname " + inetUtils.findFirstNonLoopbackHostInfo().getHostname());
    }
}
