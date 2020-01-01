package com.shephertz.app42.gaming.api.storage;

import com.shephertz.app42.gaming.api.client.App42ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StorageResponseBuilder extends App42ResponseBuilder {
	public static void main(String[] args) throws Exception {
		Storage storage = new Storage();
		storage.new JSONDocument();
		(new StorageResponseBuilder()).buildResponse(
				"{\"app42\":{\"response\":{\"success\":true,\"storage\":{\"dbName\":\"db\",\"collectionName\":\"col\",\"jsonDoc\":[{\"_id\":{\"$oid\":\"4f6af8b56cba3551222b5db8\"},\"nae\":\"test\"},{\"_id\":{\"$oid\":\"4f6af8fb6cba3551222b5db9\"},\"nae\":\"test\"},{\"_id\":{\"$oid\":\"4f6af9b66cba3551222b5dba\"},\"nae\":\"test\"}]}}}}");
	}

	public Storage buildResponse(String json) throws Exception {
		Storage storageObj = new Storage();
		List<Storage.JSONDocument> jsonDocList = new ArrayList<>();
		storageObj.setJsonDocList(jsonDocList);
		storageObj.setStrResponse(json);
		JSONObject jsonObj = new JSONObject(json);
		JSONObject jsonObjApp42 = jsonObj.getJSONObject("app42");
		JSONObject jsonObjResponse = jsonObjApp42.getJSONObject("response");
		storageObj.setResponseSuccess(jsonObjResponse.getBoolean("success"));
		JSONObject jsonObjStorage = jsonObjResponse.getJSONObject("storage");
		this.buildObjectFromJSONTree(storageObj, jsonObjStorage);
		if (jsonObjStorage.has("jsonDoc")) {
			if (jsonObjStorage.get("jsonDoc") instanceof JSONObject) {
				JSONObject jsonObjDoc = jsonObjStorage.getJSONObject("jsonDoc");
				Storage.JSONDocument document = storageObj.new JSONDocument();
				this.buildJsonDocument(document, jsonObjDoc);
			} else {
				JSONArray jsonObjDocArray = jsonObjStorage.getJSONArray("jsonDoc");

				for (int i = 0; i < jsonObjDocArray.length(); ++i) {
					JSONObject jsonObjDoc = jsonObjDocArray.getJSONObject(i);
					Storage.JSONDocument document = storageObj.new JSONDocument();
					this.buildJsonDocument(document, jsonObjDoc);
				}
			}

		}
		return storageObj;
	}

	private void buildJsonDocument(Storage.JSONDocument document, JSONObject jsonObjDoc) throws Exception {
		JSONObject jsonObjOwner;
		String owner;
		if (jsonObjDoc.has("_id")
				&& !jsonObjDoc.get("_id").getClass().getCanonicalName().equals("org.json.JSONObject.Null")) {
			jsonObjOwner = jsonObjDoc.getJSONObject("_id");
			owner = jsonObjOwner.getString("$oid");
			document.setDocId(owner);
			jsonObjDoc.remove("_id");
		}

		String updateId;
		if (jsonObjDoc.has("_$createdAt")
				&& !jsonObjDoc.get("_$createdAt").getClass().getCanonicalName().equals("org.json.JSONObject.Null")) {
			updateId = jsonObjDoc.getString("_$createdAt");
			document.setCreatedAt(updateId);
			jsonObjDoc.remove("_$createdAt");
		}

		if (jsonObjDoc.has("_$updatedAt")
				&& !jsonObjDoc.get("_$updatedAt").getClass().getCanonicalName().equals("org.json.JSONObject.Null")) {
			updateId = jsonObjDoc.getString("_$updatedAt");
			document.setUpdatedAt(updateId);
			jsonObjDoc.remove("_$updatedAt");
		}

		if (jsonObjDoc.has("_$event")
				&& !jsonObjDoc.get("_$event").getClass().getCanonicalName().equals("org.json.JSONObject.Null")) {
			updateId = jsonObjDoc.getString("_$event");
			document.setEvent(updateId);
			jsonObjDoc.remove("_$event");
		}

		if (jsonObjDoc.has("_$owner")
				&& !jsonObjDoc.get("_$owner").getClass().getCanonicalName().equals("org.json.JSONObject.Null")) {
			jsonObjOwner = jsonObjDoc.getJSONObject("_$owner");
			owner = jsonObjOwner.getString("owner");
			document.setOwner(owner);
			jsonObjDoc.remove("_$owner");
		}

		document.setJsonDoc(jsonObjDoc.toString());
	}
}
