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

package cl.almejo.vsim.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class ImageUtils {
	public static ImageIcon loadIcon(String icon) {
		if (icon == null) {
			return null;
		}
		try {
			return new ImageIcon(ImageIO.read(ClassLoader.getSystemResourceAsStream("icons/" + icon)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
