package com.kelan.core.wrapper;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 继承HttpServletResponseWrapper用于在Filter中截获Response并修改Response的内容
 * @author WYX
 *
 */
public class StatusExposingServletResponse extends HttpServletResponseWrapper {   
	 
    private int httpStatus;   
   
    public StatusExposingServletResponse(HttpServletResponse response) {   
        super(response);   
    }   
   
    @Override   
    public void sendError(int sc) throws IOException {   
        httpStatus = sc;   
        super.sendError(sc);   
    }   
   
    @Override   
    public void sendError(int sc, String msg) throws IOException {   
        httpStatus = sc;   
        super.sendError(sc, msg);   
    }   
   
   
    @Override   
    public void setStatus(int sc) {   
        httpStatus = sc;   
        super.setStatus(sc);   
    }   
      
    public int getStatus() {   
        return httpStatus;   
    }   
   
}  
