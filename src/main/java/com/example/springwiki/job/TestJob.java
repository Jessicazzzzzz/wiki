//package com.example.springwiki.job;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @author jessica~
// * @version 1.0
// */
//@Component
//public class TestJob {
//    private static final Logger LOG = LoggerFactory.getLogger(TestJob.class);
//
//    /**
//     * 固定时间间隔
//     */
//    @Scheduled(fixedRate = 1000)
//    public void simple() throws InterruptedException {
//        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
//        String dateString = sdf.format(new Date());
//        Thread.sleep(2000);
//        LOG.info("每隔2秒执行一次:{}", dateString);
//    }
//
//    /**
//     * cron 跑批
//     */
//    @Scheduled(cron = "*/1 * * * * ?")
//    public void cron() throws InterruptedException {
//        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
//        String dateString = sdf.format(new Date());
//        Thread.sleep(1500);
//        LOG.info("每隔1.5秒执行一次:{}", dateString);
//    }
//}
