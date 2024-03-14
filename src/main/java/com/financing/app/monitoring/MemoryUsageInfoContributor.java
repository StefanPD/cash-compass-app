package com.financing.app.monitoring;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

@Component
public class MemoryUsageInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        Map<String, String> heapMemoryDetails = createMemoryDetailsMap(heapMemoryUsage);
        Map<String, String> nonHeapMemoryDetails = createMemoryDetailsMap(nonHeapMemoryUsage);

        Map<String, Object> memoryUsageDetails = new HashMap<>();
        memoryUsageDetails.put("heap", heapMemoryDetails);
        memoryUsageDetails.put("nonHeap", nonHeapMemoryDetails);

        builder.withDetail("memoryUsage", memoryUsageDetails);
    }

    private String formatBytesToMB(long bytes) {
        return String.format("%d MB", bytes / 1024 / 1024);
    }

    private Map<String, String> createMemoryDetailsMap(MemoryUsage memoryUsage) {
        Map<String, String> memoryDetails = new HashMap<>();
        memoryDetails.put("init", formatBytesToMB(memoryUsage.getInit()));
        memoryDetails.put("used", formatBytesToMB(memoryUsage.getUsed()));
        memoryDetails.put("committed", formatBytesToMB(memoryUsage.getCommitted()));
        memoryDetails.put("max", formatBytesToMB(memoryUsage.getMax()));
        return memoryDetails;
    }

}
