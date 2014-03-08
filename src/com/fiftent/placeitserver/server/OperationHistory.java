package com.fiftent.placeitserver.server;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;

public class OperationHistory {
	
	public enum OperationType{
		CREATE(1),CHANGE_STATUS(2),DELETE(3);
		private int value;
		private OperationType(int value){
			this.value = value;
		}
		public int getValue(){
			return value;
		}
		public static OperationType getOperationType(int value){
			switch(value){
			case 1:
				return CREATE;
			case 2:
				return CHANGE_STATUS;
			case 3:
				return DELETE;
			default:
				return null;
			}
		}
	}
	public static void createOrUpdateOperationHistory(String username, Date time, long id, OperationType operation){
		Key key = KeyFactory.createKey(Constant.USER_KEY, username);
		Entity operationHistory = new Entity(Constant.OPERATION_HISTROTY_KEY, time.getTime(), key);
		operationHistory.setProperty(Constant.PLACE_IT_KEY, id);
		operationHistory.setProperty(Constant.TIME, time);
		operationHistory.setProperty(Constant.OPERATION_TYPE, operation);
	}
	
	
	
}
