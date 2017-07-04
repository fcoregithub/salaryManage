package cn.com.flaginfo.utils;

import java.util.MissingResourceException;

public class SystemConfig {
	private SystemConfig() {
	}

	/**
	 * get the value from the properties file
	 * 
	 * @param key
	 *            the key in the properties file
	 * @return
	 */
	public static String getString(String key) {
		try {
			/*return RESOURCE_BUNDLE.getString(key);*/
			return CustomPropertyPlaceholderConfigurer.getProperty(key, null);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

