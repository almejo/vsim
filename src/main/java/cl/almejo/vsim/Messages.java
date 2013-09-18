package cl.almejo.vsim;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: alejo
 * Date: 9/16/13
 * Time: 11:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class Messages {
	private static final String BUNDLE_NAME = "i18n.messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String t(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static Character c(String key) {
		try {
			String string = RESOURCE_BUNDLE.getString(key);
			return string.trim().charAt(0);
		} catch (MissingResourceException e) {
			return '_';
		}
	}
}
