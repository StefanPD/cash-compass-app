package com.financing.app.monitoring;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

@Component
public class ThreadInfoContributor implements InfoContributor {

    @Override
    public void contribute(Builder builder) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        int totalThreadCount = threadMXBean.getThreadCount();
        long[] threadIds = threadMXBean.getAllThreadIds();
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds);

        Map<String, Integer> threadStatesCount = new HashMap<>();
        for (ThreadInfo threadInfo : threadInfos) {
            if (threadInfo != null) {
                Thread.State state = threadInfo.getThreadState();
                threadStatesCount.put(state.toString(), threadStatesCount.getOrDefault(state.toString(), 0) + 1);
            }
        }

        Map<String, Object> threadDetails = new HashMap<>();
        threadDetails.put("totalThreads", totalThreadCount);
        threadDetails.put("threadStates", threadStatesCount);

        builder.withDetail("threads", threadDetails);
    }
}
