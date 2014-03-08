package com.fiftent.placeitserver.server;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class User {

	public static void createOrUpdateUser(String username, String password){
		Entity user = getUser(username);
		if(user == null){
			user = new Entity(Constant.USER_KEY, username);
		}
		user.setProperty(Constant.PASSWORD_KEY, password);
		DataStoreUtil.persistEntity(user);
	}
	

	public static Entity getUser(String username) {
		Key key = KeyFactory.createKey(Constant.USER_KEY,username);
		return DataStoreUtil.findEntity(key);
	}
	
	public static Iterable<Entity> getAllUsers(){
		return DataStoreUtil.listEntities(Constant.USER_KEY, null, null);
	}
	
	public static boolean verifyUser(String username, String password){
		Entity user = getUser(username);
		if(user == null)
			return false;
		String p = user.getProperty(Constant.PASSWORD_KEY).toString();
		return password.equals(p);
	}
	
	public static boolean isUsernameExist(String username){
		return getUser(username) != null;
	}
	
	public static List<Entity> getRegIds(String username){
		Key key = KeyFactory.createKey(Constant.USER_KEY, username);
		Query q = new Query(Constant.GCM_ID_KEY).setAncestor(key);
		PreparedQuery pq = DataStoreUtil.getDatastoreServiceInstance().prepare(q);
		List<Entity> results =  pq.asList(FetchOptions.Builder.withDefaults());
		return results;
	}
	
	public static List<Entity> getPlaceIts(String username){
		Key key = KeyFactory.createKey(Constant.USER_KEY, username);
		Query q = new Query(Constant.PLACE_IT_KEY).setAncestor(key);
		PreparedQuery pq = DataStoreUtil.getDatastoreServiceInstance().prepare(q);
		List<Entity> results =  pq.asList(FetchOptions.Builder.withDefaults());
		return results;
	}

	public static List<Entity> getOperationHistory(String username, Date lastUpdate){
		Key key = KeyFactory.createKey(Constant.USER_KEY, username);
		Query q = new Query(Constant.OPERATION_HISTROTY_KEY)
					.setAncestor(key).setFilter(
						new Query.FilterPredicate(
						Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.GREATER_THAN, lastUpdate.getTime()));
		PreparedQuery pq = DataStoreUtil.getDatastoreServiceInstance().prepare(q);
		List<Entity> results =  pq.asList(FetchOptions.Builder.withDefaults());
		return results;
	}
	
	public static List<Entity> getOperationHistory(String username){
		Key key = KeyFactory.createKey(Constant.USER_KEY, username);
		Query q = new Query(Constant.OPERATION_HISTROTY_KEY)
					.setAncestor(key);
		PreparedQuery pq = DataStoreUtil.getDatastoreServiceInstance().prepare(q);
		List<Entity> results =  pq.asList(FetchOptions.Builder.withDefaults());
		return results;
	}
}
