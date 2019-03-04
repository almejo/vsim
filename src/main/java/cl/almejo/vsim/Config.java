package cl.almejo.vsim;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */

public class Config {
	static String getColorsResource() {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(Main.class.getClassLoader().getResourceAsStream("config/colors.json"), writer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	static void init() throws IOException {
		File file = new File(System.getProperty("user.home") + File.separator + ".vsim/colors.json");
		if (!file.exists()) {
			new File(System.getProperty("user.home") + File.separator + ".vsim").mkdirs();
		}
		writeColors(getColorsResource());
	}

	public static String readColors() throws IOException {
		File file = new File(System.getProperty("user.home") + File.separator + ".vsim/colors.json");
		return FileUtils.readFileToString(file, "UTF-8");
	}

	public static void writeColors(String data) throws IOException {
		FileUtils.writeStringToFile(new File(System.getProperty("user.home") + File.separator + ".vsim/colors.json"), data, "UTF-8");
	}
}
