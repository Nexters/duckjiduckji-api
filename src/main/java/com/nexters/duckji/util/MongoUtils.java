package com.nexters.duckji.util;

import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.core.query.Update;

public class MongoUtils {

	/**
	 * convert only for 1depth key
	 */
	public static Update toUpdate(Map<String, Object> map) {
		Update update = new Update();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object value = map.get(key);
			if (value == null) {
				update.unset(key);
			}
			else {
				update.set(key, value);
			}
		}

		return update;
	}
}
