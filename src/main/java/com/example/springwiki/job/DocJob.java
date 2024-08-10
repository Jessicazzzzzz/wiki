package com.example.springwiki.job;

import com.example.springwiki.service.DocService;
import com.example.springwiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jessica~
 * @version 1.0
 */
@Component
public class DocJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);
    @Resource
    private DocService docService;
    @Resource
    private SnowFlake snowFlake;

    /**
     * cron 跑批
     * 每30秒跟新电子书的信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("跟新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书下的文档数据结束,耗时:{}毫秒", System.currentTimeMillis() - start);
    }
}
