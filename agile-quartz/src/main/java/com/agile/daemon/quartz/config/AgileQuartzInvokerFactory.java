package com.agile.daemon.quartz.config;

import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.event.SysJobEvent;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Aspect
@Service
@AllArgsConstructor
public class AgileQuartzInvokerFactory {

    private final ApplicationEventPublisher publisher;

    @SneakyThrows
    void init(SysJob sysJob, Trigger trigger) {
        publisher.publishEvent(new SysJobEvent(sysJob, trigger));
    }

}
