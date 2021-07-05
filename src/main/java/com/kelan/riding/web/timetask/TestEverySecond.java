package com.kelan.riding.web.timetask;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component("TestEverySecond")
public class TestEverySecond  {
	 public void show(){  
	        System.out.println("XMl:is show run");  
	    }  
}
