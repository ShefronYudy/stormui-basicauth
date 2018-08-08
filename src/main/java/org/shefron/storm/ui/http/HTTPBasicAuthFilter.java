package org.shefron.storm.ui.http;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HTTPBasicAuthFilter implements Filter {
	
	private boolean enabled = false;
	private String username = "admin";
	private String password = "admin";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		
		if(!enabled) {
			filterChain.doFilter(req, res);
		} else {
			HttpServletRequest request = (HttpServletRequest)req;
			HttpServletResponse response = (HttpServletResponse)res;
			
			String authHeader = request.getHeader("Authorization");
			if (authHeader == null) {
				authHeader = "";
			} else {
				String[] split = authHeader.split(" ", 2);
				if (split.length != 2 || !split[0].equalsIgnoreCase("Basic")) {
					authHeader = "";
				} else {
					authHeader = new String(Base64.getDecoder().decode(split[1]));
				}
			}
			
			boolean authorized = true;
			if (!authHeader.isEmpty()) {
				String[] userAndPassword = authHeader.split(":", 2);
				String givenUser = userAndPassword[0];
				String givenPass = userAndPassword[1];
				if (this.username.equals(givenUser) && this.password.equals(givenPass)) {
					filterChain.doFilter(req, res);
				} else {
					authorized = false;
				}
			} else {
				authorized = false;
			}
			
			if(!authorized) {
				response.setStatus(401);
				response.setHeader("Cache-Control", "no-store");
				response.setDateHeader("Expires", 0);
				response.setHeader("WWW-authenticate", "Basic Realm=\"Restricted\"");
				response.getWriter().println("Authentication Required");
			}
			
		}
	}
	


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		/** 初始化认证信息 */
		
		String enabled = filterConfig.getInitParameter("http.basic.enabled");
		if(enabled != null) {
			this.enabled = Boolean.parseBoolean(enabled);
		}
		
		String user = filterConfig.getInitParameter("http.basic.username");
		if(user != null) {
			this.username = user;
		}
		
		String pass = filterConfig.getInitParameter("http.basic.password");
		if(pass != null) {
			this.password = pass;
		}
		
	}
	
}
