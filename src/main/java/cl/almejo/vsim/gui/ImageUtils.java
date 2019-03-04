package cl.almejo.vsim.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class ImageUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageIcon.class);

	public static ImageIcon loadIcon(String icon) {
		if (icon == null) {
			return null;
		}
		try {
			LOGGER.debug("loading " + "icons/" + icon);
			return new ImageIcon(ImageIO.read(ImageUtils.class.getClassLoader().getSystemResourceAsStream("icons/" + icon)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
