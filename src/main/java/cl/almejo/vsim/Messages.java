/**
 *
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


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
