package com.fiftent.placeitserver.server;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;


public class PlaceIt {
	public static void createOrUpdatePlaceIt(String username, long id, String title, String description, int repeatedDayInWeek,
			int repeatedMinute, int numOfWeekRepeat, Date createDate, Date postDate, double latitude, double longitude,
			int status, String[] categories){
		Entity user = User.getUser(username);
		if(user == null)
			return ;
		Entity pi = new Entity(Constant.PLACE_IT_KEY, id, user.getKey());
		if(pi == null)
			return ;
		pi.setProperty(Constant.TITLE, title);
		pi.setProperty(Constant.DESCRIPTION, description);
		if(repeatedDayInWeek != 0)
			pi.setProperty(Constant.REPEATED_DAY_IN_WEEK, repeatedDayInWeek);
		if(repeatedMinute != 0)
			pi.setProperty(Constant.REPEATED_MINUTE, repeatedMinute);
		if(numOfWeekRepeat != 0)
			pi.setProperty(Constant.NUM_OF_WEEK_REPEAT, numOfWeekRepeat);
		pi.setProperty(Constant.CREATE_DATE, createDate);
		pi.setProperty(Constant.POST_DATE, postDate);
		if(latitude <= 90 && latitude >= -90)
			pi.setProperty(Constant.LATITUDE, latitude);
		if(longitude <=180 && longitude >= -180)
			pi.setProperty(Constant.LONGITUDE, longitude);
		pi.setProperty(Constant.STATUS, status);
		
		if(categories != null){
			switch(categories.length){
			case 3:
				pi.setProperty(Constant.CATEGORY_THREE, categories[2]);
			case 2:
				pi.setProperty(Constant.CATEGORY_TWO, categories[1]);
			case 1:
				pi.setProperty(Constant.CATEGORY_ONE, categories[0]);
			}
		}
		DataStoreUtil.persistEntity(pi);
	}
	
	public static void setStatus(String username, long id, int status){
		Entity user = User.getUser(username);
		if(user == null)
			return ;
		Entity pi = new Entity(Constant.PLACE_IT_KEY, id, user.getKey());
		if(pi == null)
			return ;
		pi.setProperty(Constant.STATUS, status);
		DataStoreUtil.persistEntity(pi);
	}
	
	public static void deletePlaceIt(String username, long id){
		Entity user = User.getUser(username);
		if(user == null)
			return ;
		Key key = KeyFactory.createKey(user.getKey(), Constant.PLACE_IT_KEY, id);
		DataStoreUtil.deleteEntity(key);
	}
	
	public static Entity getPlaceIt(String username, long id){
		Key userKey = KeyFactory.createKey(Constant.USER_KEY, username);
		Key placeItKey = KeyFactory.createKey(userKey, Constant.PLACE_IT_KEY, id);
		return DataStoreUtil.findEntity(placeItKey);
	}
}
