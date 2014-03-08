package com.fiftent.placeitserver.server;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class RegId {

	public static void createRegId(String username, String regId){
		Entity user = User.getUser(username);
		Entity reg = new Entity(Constant.GCM_ID_KEY, regId, user.getKey());
		DataStoreUtil.persistEntity(reg);
	}
	
	public static boolean isRegIdExist(String username, String regId){
		Key userKey = KeyFactory.createKey(Constant.USER_KEY, username);
		Key regIdKey = KeyFactory.createKey(userKey, Constant.GCM_ID_KEY, regId);
		return DataStoreUtil.findEntity(regIdKey) != null;
	}
	
	
}
