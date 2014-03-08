package com.fiftent.placeitserver.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestServlet extends HttpServlet{

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter(Constant.ACTION);
		PrintWriter out = resp.getWriter();
		if(action.equalsIgnoreCase("alluser")){
			String s = DataStoreUtil.writeJSON(User.getAllUsers());
			out.print(s);
		}else if(action.equalsIgnoreCase("allplaceit")){
			String username = req.getParameter(Constant.USER_NAME);
			String s = DataStoreUtil.writeJSON(User.getPlaceIts(username));
			out.print(s);
		}else if(action.equalsIgnoreCase("allhistory")){
			String username = req.getParameter(Constant.USER_NAME);
			String s = DataStoreUtil.writeJSON(User.getOperationHistory(username));
			out.print(s);
		}
		
		
	}
}
