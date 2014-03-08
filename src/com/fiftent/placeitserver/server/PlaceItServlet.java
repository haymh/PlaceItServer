package com.fiftent.placeitserver.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PlaceItServlet extends HttpServlet{

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter(Constant.ACTION);
		if(action.equalsIgnoreCase(Constant.CREATE))
			doCreate(req, resp);
		else if(action.equalsIgnoreCase(Constant.UPDATE))
			doUpdate(req, resp);
		else if(action.equalsIgnoreCase(Constant.DELETE))
			doDel(req, resp);
		String username = req.getParameter(Constant.USER_NAME);
		String password = req.getParameter(Constant.PASSWORD);
		String regId = req.getParameter(Constant.GCM_ID_KEY);
		/*
		long id, String title, String description, int repeatedDayInWeek,
		int repeatedMinute, int numOfWeekRepeat, Date createDate, Date postDate, double latitude, double longitude,
		int status, String[] categories
		*/
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
	private void doCreate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		
		doCreateOrUpdate(req, resp, OperationHistory.OperationType.CREATE);
		
		// send message to gcm, notify other devices to update
	}

	private void doUpdate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String username = (String)req.getSession().getAttribute(Constant.USER_NAME);
		if(username == null){
			//something bad should happy
			return;
		}
		long id = Long.parseLong(req.getParameter(Constant.PLACE_IT_KEY));
		int status = Integer.parseInt(req.getParameter(Constant.STATUS));
		PlaceIt.setStatus(username, id, status);
		
		// send message to gcm, notify other devices to update
	}
	private void doDel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String username = (String)req.getSession().getAttribute(Constant.USER_NAME);
		if(username == null){
			//something bad should happy
			return;
		}
		long id = Long.parseLong(req.getParameter(Constant.PLACE_IT_KEY));
		PlaceIt.deletePlaceIt(username, id);
		
		// send message to gcm, notify other devices to update
	}
	
	private void doCreateOrUpdate(HttpServletRequest req, HttpServletResponse resp, OperationHistory.OperationType operation )
			throws ServletException, IOException{
		String username = (String)req.getSession().getAttribute(Constant.USER_NAME);
		if(username == null){
			//something bad happy
			return;
		}
		long id = Long.parseLong(req.getParameter(Constant.PLACE_IT_KEY));
		String title = req.getParameter(Constant.TITLE);
		String description = req.getParameter(Constant.DESCRIPTION);
		
		
		String rdiw = req.getParameter(Constant.REPEATED_DAY_IN_WEEK);
		int repeatedDayInWeek = 0;
		if(rdiw != null)
			repeatedDayInWeek = Integer.parseInt(rdiw);
		
		
		int repeatedMinute = 0;
		String rm = req.getParameter(Constant.REPEATED_MINUTE);
		if(rm != null)
			repeatedMinute = Integer.parseInt(rm);
		
		
		int numOfWeekRepeat = 0;
		String nowr = req.getParameter(Constant.NUM_OF_WEEK_REPEAT);
		if(nowr != null)
			numOfWeekRepeat = Integer.parseInt(nowr);
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createDate = null;
		Date postDate = null;
		try {
			createDate = dateFormat.parse(req.getParameter(Constant.CREATE_DATE));
			postDate = dateFormat.parse(req.getParameter(Constant.POST_DATE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		double latitude = -91;
		String lat = req.getParameter(Constant.LATITUDE);
		if(lat != null)
			latitude = Double.parseDouble(lat);
		double longitude = -181;
		String lng = req.getParameter(Constant.LONGITUDE);
		if(lng != null)
			longitude = Double.parseDouble(lng);
		
		
		int status = Integer.parseInt(req.getParameter(Constant.STATUS));
		String categoryOne = req.getParameter(Constant.CATEGORY_ONE);
		String categoryTwo = req.getParameter(Constant.CATEGORY_TWO);
		String categoryThree = req.getParameter(Constant.CATEGORY_THREE);
		String[] categories = null;
		if(categoryOne != null && categoryTwo != null && categoryThree != null){
			categories = new String[3];
			categories[0] = categoryOne;
			categories[1] = categoryTwo;
			categories[2] = categoryThree;
		}else if (categoryOne != null && categoryTwo != null && categoryThree == null){
			categories = new String[2];
			categories[0] = categoryOne;
			categories[1] = categoryTwo;
		}else if(categoryOne != null && categoryTwo == null && categoryThree == null){
			categories = new String[1];
			categories[0] = categoryOne;
		}
		
		PlaceIt.createOrUpdatePlaceIt(username, id, title, description, repeatedDayInWeek, repeatedMinute,
				numOfWeekRepeat, createDate, postDate, latitude, longitude, status, categories);
		OperationHistory.createOrUpdateOperationHistory(username, new Date(), id, operation);
	}
}
