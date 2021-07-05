package com.kelan.riding.web.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LongTimeAsyncCallService {
    private final int CorePoolSize = 4;
    private final int NeedSeconds = 3;
    private Random random = new Random();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CorePoolSize);
    public void makeRemoteCallAndUnknownWhenFinish(LongTermTaskCallback callback){
        System.out.println("完成此任务需要 : " + NeedSeconds + " 秒");
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                callback.callback("长时间异步调用完成.");
            }
        }, 10000, TimeUnit.SECONDS);
    }
    public void saveUser(List<String> zuoyes) throws Exception {  
        System.out.println(zuoyes);
   } 
    @Async  
    public void async(){  
        System.out.println("异步开始-----------》》》"+ Thread.currentThread().getId());  
        try { 
            Thread.sleep(10*1000);  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        System.out.println("异步结束-----------》》》");  
    }  
}
