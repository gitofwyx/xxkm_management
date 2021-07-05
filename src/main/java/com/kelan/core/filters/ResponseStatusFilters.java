package com.kelan.core.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import com.kelan.core.wrapper.StatusExposingServletResponse;

/**
 * Servlet Filter implementation class ResponseStatusFilters
 */
@WebFilter("/ResponseStatusFilters")
public class ResponseStatusFilters implements Filter {

    /**
     * Default constructor. 
     */
    public ResponseStatusFilters() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		StatusExposingServletResponse  resp = new StatusExposingServletResponse((HttpServletResponse)response);  
        resp.setStatus(HttpServletResponse.SC_OK );  
        int Hcode =  resp.getStatus();
        request.setAttribute("Hcode",Hcode);
        chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
