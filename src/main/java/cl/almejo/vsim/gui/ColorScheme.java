/**
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: alejo
 */

package cl.almejo.vsim.gui;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gates.Pin;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ColorScheme {

	private HashMap<String, Color> _colors = new HashMap<String, Color>();

	private static ColorScheme _current;

	private static HashMap<String, ColorScheme> _schemes = new HashMap<String, ColorScheme>();

	static {

		File file = new File("colors.json");
		try {
			String json = FileUtils.readFileToString(file);

			Map<String, Map<String, String>> schemes = (Map<String, Map<String, String>>) new ObjectMapper().readValue(json, Map.class);
			for (String name : schemes.keySet()) {
				Map<String, String> map = schemes.get(name);
				ColorScheme scheme = new ColorScheme(name);

				for (String colorName : map.keySet()) {
					scheme.set(colorName, new Color(Integer.parseInt(map.get(colorName).replace("#", ""), 16), false));
				}
				_schemes.put(name, scheme);
			}
			_current = _schemes.get("default");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String _name;

	public ColorScheme(String name) {
		_name = name;
		_colors.put("background", Color.GRAY);
		_colors.put("bus-on", Color.RED);
		_colors.put("gates", Color.BLUE);
		_colors.put("ground", Color.GREEN);
		_colors.put("off", Color.BLACK);
		_colors.put("wires-on", Color.RED);
	}

	public static String save() throws IOException {
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		for (String schemeName : _schemes.keySet()) {
			ColorScheme scheme = _schemes.get(schemeName);
			HashMap<String, String> colors = new HashMap<String, String>();
			for (String colorName : scheme._colors.keySet()) {
				Color color = scheme._colors.get(colorName);
				colors.put(colorName, "#" + Integer.toHexString(color.getRGB()).substring(2));
			}
			map.put(schemeName, colors);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
	}

	public static ColorScheme getCurrent() {
		return _current;
	}

	public static String[] getNames() {
		return _schemes.keySet().toArray(new String[_schemes.keySet().size()]);
	}

	public static void setCurrent(String name) {
		_current = _schemes.get(name);
	}

	public void set(String colorName, Color color) {
		_colors.put(colorName, color);
	}

	public Color get(String name) {
		return _colors.get(name);
	}

	public static Color getBackground() {
		return _current.get("background");
	}

	public static Color getBusOn() {
		return _current.get("bus-on");
	}

	public static Color getGates() {
		return _current.get("gates");
	}

	public static Color getGround() {
		return _current.get("ground");
	}

	public static Color getWireOn() {
		return _current.get("wires-on");
	}

	public static Color getOff() {
		return _current.get("off");
	}

	public static Color getColor(Pin pin) {
		return pin.getInValue() == Constants.THREE_STATE ? getGround()
				: pin.getInValue() == Constants.ON ? getWireOn() : getOff();
	}

	public static ColorScheme getScheme(String name) {
		return _schemes.get(name);
	}

	public static void add(String name) {
		_schemes.put(name, new ColorScheme(name));
		_current = _schemes.get(name);
	}

	public String getName() {
		return _name;
	}

	public static boolean exists(String name) {
		for (String schemeName : _schemes.keySet()) {
			if (schemeName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}

