package com.example.springwiki.job;

import com.example.springwiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * cron 跑批
     * 每30秒跟新电子书的信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {

        docService.updateEbookInfo();
    }
}
