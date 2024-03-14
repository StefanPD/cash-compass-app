package com.financing.app.monitoring;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Component
public class RuntimeInfoContributor implements InfoContributor {

    private final Instant startTime = Instant.now();

    private final BuildProperties buildProperties;

    public RuntimeInfoContributor(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    public void contribute(Builder builder) {
        builder.withDetail("runtime", Map.of(
                "javaVersion", System.getProperty("java.version"),
                "springBootVersion", buildProperties.getVersion(),
                "uptime", getUptime()
        ));
    }

    private String getUptime() {
        Duration uptime = Duration.between(startTime, Instant.now());
        return uptime.toString();
    }
}
