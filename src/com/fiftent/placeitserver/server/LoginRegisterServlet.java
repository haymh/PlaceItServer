package com.fiftent.placeitserver.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;


public class LoginRegisterServlet extends HttpServlet{
	
	

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter(Constant.ACTION);
		String username = req.getParameter(Constant.USER_NAME);
		String password = req.getParameter(Constant.PASSWORD);
		String regId = req.getParameter(Constant.GCM_ID_KEY);
		PrintWriter out = resp.getWriter();
		if(action.equalsIgnoreCase(Constant.LOGIN)){
			if (User.verifyUser(username, password)){
				HttpSession session = req.getSession(true);
				session.setAttribute(Constant.USER_NAME, username);
				session.setAttribute(Constant.PASSWORD, password);
				session.setAttribute(Constant.GCM_ID_KEY, regId);
				if(!RegId.isRegIdExist(username, regId))
					RegId.createRegId(username, regId);
				out.print("<html><body>user info is verified</body></html>");
			} else {
				out.print("<html><body>login failed</body></html>");
			}
		}else if(action.equalsIgnoreCase(Constant.REGISTER)){
			if(User.isUsernameExist(username))
				out.print("<html><body>register failed,username exists</body></html>");
			else{
				User.createOrUpdateUser(username, password);
				RegId.createRegId(username, regId);
				HttpSession session = req.getSession(true);
				session.setAttribute(Constant.USER_NAME, username);
				session.setAttribute(Constant.PASSWORD, password);
				session.setAttribute(Constant.GCM_ID_KEY, regId);
				out.print("<html><body>register successfully</body></html>");
			}
		}
		
		
	}
}
