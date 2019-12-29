package com.shephertz.app42.gaming.api.storage;

import com.shephertz.app42.gaming.api.client.App42Exception;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Date;

public class QueryBuilder {
	public static Query build(String key, Object value, QueryBuilder.Operator op) {
		Query query;

		try {
			if (value instanceof Date) {
				Date date = (Date) value;
				value = Util.getUTCFormattedTimestamp(date);
			} else if (value instanceof List) {
				value = value.toString();
			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("key", key);
			jsonObj.put("value", value);
			jsonObj.put("operator", op.getValue());
			query = new Query(jsonObj);
		} catch (Exception var6) {
			throw new App42Exception(var6);
		}

		return query;
	}

	public static Query setLoggedInUser(String logged) {
		Query query;

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("key", "_$owner.owner");
			jsonObj.put("value", logged);
			jsonObj.put("operator", QueryBuilder.Operator.EQUALS.getValue());
			query = new Query(jsonObj);
			return query;
		} catch (Exception var3) {
			throw new App42Exception(var3);
		}
	}

	public static Query setCreatedOn(String date, QueryBuilder.Operator op) {
		Query query;

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("key", "_$createdAt");
			jsonObj.put("value", date);
			jsonObj.put("operator", op.getValue());
			query = new Query(jsonObj);
			return query;
		} catch (Exception var4) {
			throw new App42Exception(var4);
		}
	}

	public static Query setUpdatedOn(String date, QueryBuilder.Operator op) {
		Query query;

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("key", "_$updatedAt");
			jsonObj.put("value", date);
			jsonObj.put("operator", op.getValue());
			query = new Query(jsonObj);
			return query;
		} catch (Exception var4) {
			throw new App42Exception(var4);
		}
	}

	public static Query setDocumentId(String docid) {
		Query query;

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("key", "_id");
			jsonObj.put("value", docid);
			jsonObj.put("operator", QueryBuilder.Operator.EQUALS.getValue());
			query = new Query(jsonObj);
			return query;
		} catch (Exception var3) {
			throw new App42Exception(var3);
		}
	}

	public static Query compoundOperator(Query q1, QueryBuilder.Operator op, Query q2) {
		JSONArray jsonArray = new JSONArray();
		Query query = new Query(jsonArray);

		try {
			if (q1.getType() instanceof JSONObject) {
				jsonArray.put(q1.getType());
			} else {
				jsonArray.put(q1.getType());
			}

			jsonArray.put(new JSONObject("{'compoundOpt':'" + op.getValue() + "'}"));
			jsonArray.put(q2.getType());

			return query;
		} catch (JSONException var6) {
			throw new App42Exception(var6);
		}
	}

	public enum GeoOperator {
		NEAR("$near"), WITHIN("$within");

		private String value;

		GeoOperator(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum Operator {
		EQUALS("$eq"), NOT_EQUALS("$ne"), GREATER_THAN("$gt"), LESS_THAN("$lt"), GREATER_THAN_EQUALTO(
				"$gte"), LESS_THAN_EQUALTO("$lte"), LIKE("$lk"), AND("$and"), OR("$or"), INLIST("$in");

		private String value;

		Operator(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
}
