package com.shephertz.app42.gaming.api.storage;

import com.shephertz.app42.gaming.api.client.*;
import com.shephertz.app42.gaming.multiplayer.client.JavaWarpClient;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONObject;

import java.util.Hashtable;

public class StorageService extends App42Service {
	private String resource = "storage";
	private String version = "1.0";

	public App42Response findAllDocuments(String dbName, String collectionName, int max, int offset)
			throws App42Exception {
		String response;
		Storage storage;

		try {
			Hashtable<String, String> queryParams = new Hashtable<>();
			Hashtable<String, String> headerParams = new Hashtable<>();
			Hashtable<String, String> signParams = this.populateSignParams(JavaWarpClient.getInstance().getAPIKey());
			Hashtable<String, String> metaHeaders = this.populateMetaHeaderParams();
			headerParams.putAll(signParams);
			headerParams.putAll(metaHeaders);
			signParams.put("dbName", dbName);
			signParams.put("collectionName", collectionName);
			signParams.put("max", "" + max);
			signParams.put("offset", "" + offset);
			String signature = Util.sign(JavaWarpClient.getInstance().getPrivateKey(), signParams);
			headerParams.put("signature", signature);
			String resourceURL = this.version + "/" + this.resource + "/findAll/dbName/" + dbName + "/collectionName/"
					+ collectionName + "/" + max + "/" + offset;
			response = RESTConnectorAsync.getInstance().executeGet(resourceURL, queryParams, headerParams);
			storage = (new StorageResponseBuilder()).buildResponse(response);
			return storage;
		} catch (App42Exception var13) {
			throw var13;
		} catch (Exception var14) {
			throw new App42Exception(var14);
		}
	}

	public void findAllDocuments(final String dbName, final String collectionName, final int max, final int offset,
			final App42CallBack callBack) throws App42Exception {
		(new Thread(() -> {
			try {
				Storage storage = (Storage) StorageService.this.findAllDocuments(dbName, collectionName, max, offset);
				callBack.onSuccess(storage);
			} catch (App42Exception var2) {
				callBack.onException(var2);
			}

		})).start();
	}

	public Storage insertJSONDocument(String dbName, String collectionName, JSONObject json) throws App42Exception {
		String response;
		Storage storage;

		try {
			Hashtable<String, String> queryParams = new Hashtable<>();
			Hashtable<String, String> headerParams = new Hashtable<>();
			Hashtable<String, String> signParams = this.populateSignParams(JavaWarpClient.getInstance().getAPIKey());
			Hashtable<String, String> metaHeaders = this.populateMetaHeaderParams();
			headerParams.putAll(signParams);
			headerParams.putAll(metaHeaders);
			signParams.put("dbName", dbName);
			signParams.put("collectionName", collectionName);
			JSONObject obj = new JSONObject();
			obj.put("jsonDoc", json);
			StringBuilder sb = new StringBuilder();
			sb.append("{\"app42\":{\"storage\":").append(obj.toString()).append("}}");
			signParams.put("body", sb.toString());
			String signature = Util.sign(JavaWarpClient.getInstance().getPrivateKey(), signParams);
			headerParams.put("signature", signature);
			String resourceURL = this.version + "/" + this.resource + "/insert/dbName/" + dbName + "/collectionName/"
					+ collectionName;
			response = RESTConnectorAsync.getInstance().executePost(resourceURL, queryParams, sb.toString(),
					headerParams);
			storage = (new StorageResponseBuilder()).buildResponse(response);
			return storage;
		} catch (App42Exception var14) {
			throw var14;
		} catch (Exception var15) {
			throw new App42Exception(var15);
		}
	}

	public void insertJSONDocument(final String dbName, final String collectionName, final JSONObject json,
			App42CallBack callBack) throws App42Exception {
		(new Thread(() -> {
			try {
				StorageService.this.insertJSONDocument(dbName, collectionName, json);
			} catch (App42Exception var2) {
			}

		})).start();
	}

	public Storage findDocumentsByQueryWithPaging(String dbName, String collectionName, Query query, int max,
			int offset) throws App42Exception {
		String response;
		Storage storage;

		try {
			Hashtable<String, String> queryParams = new Hashtable<>();
			Hashtable<String, String> headerParams = new Hashtable<>();
			Hashtable<String, String> signParams = this.populateSignParams(JavaWarpClient.getInstance().getAPIKey());
			Hashtable<String, String> metaHeaders = this.populateMetaHeaderParams();
			headerParams.putAll(signParams);
			headerParams.putAll(metaHeaders);
			signParams.put("dbName", dbName);
			signParams.put("collectionName", collectionName);
			signParams.put("jsonQuery", query.getStr());
			signParams.put("max", "" + max);
			signParams.put("offset", "" + offset);
			queryParams.put("jsonQuery", query.getStr());
			String signature = Util.sign(JavaWarpClient.getInstance().getPrivateKey(), signParams);
			headerParams.put("signature", signature);
			String resourceURL = this.version + "/" + this.resource + "/findDocsByQuery/dbName/" + dbName
					+ "/collectionName/" + collectionName + "/" + max + "/" + offset;
			response = RESTConnectorAsync.getInstance().executeGet(resourceURL, queryParams, headerParams);
			Util.trace("Chat History " + response);
			storage = (new StorageResponseBuilder()).buildResponse(response);
			return storage;
		} catch (App42Exception var14) {
			throw var14;
		} catch (Exception var15) {
			throw new App42Exception(var15);
		}
	}

	public void findDocumentsByQueryWithPaging(final String dbName, final String collectionName, final Query query,
			final int max, final int offset, final App42CallBack callBack) throws App42Exception {
		(new Thread(() -> {
			try {
				Storage storage = StorageService.this.findDocumentsByQueryWithPaging(dbName, collectionName, query, max,
						offset);
				callBack.onSuccess(storage);
			} catch (App42Exception var2) {
				callBack.onException(var2);
			}

		})).start();
	}
}
