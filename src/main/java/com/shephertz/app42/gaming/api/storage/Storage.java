package com.shephertz.app42.gaming.api.storage;

import com.shephertz.app42.gaming.api.client.App42Response;

import java.util.ArrayList;

public class Storage extends App42Response {
	public String dbName;
	public String docId;
	public String collectionName;
	public Integer recordCount;
	public ArrayList<JSONDocument> jsonDocList = new ArrayList<>();

	public String getDbName() {
		return this.dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getCollectionName() {
		return this.collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Integer getRecordCount() {
		return this.recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public ArrayList<JSONDocument> getJsonDocList() {
		return this.jsonDocList;
	}

	public void setJsonDocList(ArrayList<JSONDocument> jsonDocList) {
		this.jsonDocList = jsonDocList;
	}

	public class JSONDocument {
		public String jsonDoc;
		public String createdAt;
		public String updatedAt;
		public String event;
		public String docId;
		private String owner;

		public JSONDocument() {
			Storage.this.jsonDocList.add(this);
		}

		public String getJsonDoc() {
			return this.jsonDoc;
		}

		public void setJsonDoc(String jsonDoc) {
			this.jsonDoc = jsonDoc;
		}

		public String getDocId() {
			return this.docId;
		}

		public void setDocId(String docId) {
			this.docId = docId;
		}

		public String getCreatedAt() {
			return this.createdAt;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}

		public String getUpdatedAt() {
			return this.updatedAt;
		}

		public void setUpdatedAt(String updatedAt) {
			this.updatedAt = updatedAt;
		}

		public String getEvent() {
			return this.event;
		}

		public void setEvent(String event) {
			this.event = event;
		}

		public String toString() {
			return this.docId != null && this.jsonDoc != null ? this.docId + " : " + this.jsonDoc : super.toString();
		}

		public String getOwner() {
			return this.owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}
	}
}
