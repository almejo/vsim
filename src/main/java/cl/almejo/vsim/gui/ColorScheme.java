package cl.almejo.vsim.gui;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gates.Pin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class ColorScheme {

	private final HashMap<String, Color> colors = new HashMap<>();

	private static ColorScheme currentScheme;

	private static final HashMap<String, ColorScheme> SCHEMES = new HashMap<>();

	static {

		try {
			File file = new File("colors.json");
			String json = FileUtils.readFileToString(file, "UTF-8");

			@SuppressWarnings("unchecked") Map<String, Map<String, String>> schemes = (Map<String, Map<String, String>>) new ObjectMapper().readValue(json, Map.class);
			for (String name : schemes.keySet()) {
				Map<String, String> map = schemes.get(name);
				ColorScheme scheme = new ColorScheme(name);

				map.keySet().forEach(colorName -> scheme.set(colorName, new Color(Integer.parseInt(map.get(colorName).replace("#", ""), 16), false)));
				SCHEMES.put(name, scheme);
			}
			currentScheme = SCHEMES.get("default");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final String name;

	private ColorScheme(String name) {
		this.name = name;
		colors.put("background", Color.GRAY);
		colors.put("bus-on", Color.RED);
		colors.put("gates", Color.BLUE);
		colors.put("ground", Color.GREEN);
		colors.put("off", Color.BLACK);
		colors.put("wires-on", Color.RED);
		colors.put("signal", Color.RED);
		colors.put("grid", Color.GRAY);
		colors.put("label", Color.YELLOW);
	}

	public static String save() throws IOException {
		HashMap<String, HashMap<String, String>> map = new HashMap<>();
		for (String schemeName : SCHEMES.keySet()) {
			ColorScheme scheme = SCHEMES.get(schemeName);
			HashMap<String, String> colors = new HashMap<>();
			for (String colorName : scheme.colors.keySet()) {
				Color color = scheme.colors.get(colorName);
				colors.put(colorName, "#" + Integer.toHexString(color.getRGB()).substring(2));
			}
			map.put(schemeName, colors);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
	}

	public static ColorScheme getCurrent() {
		return currentScheme;
	}

	public static String[] getNames() {
		return SCHEMES.keySet().toArray(new String[SCHEMES.keySet().size()]);
	}

	public static void setCurrent(String name) {
		currentScheme = SCHEMES.get(name);
	}

	public static Color getGrid() {
		return currentScheme.get("grid");
	}

	public void set(String colorName, Color color) {
		colors.put(colorName, color);
	}

	public Color get(String name) {
		return colors.get(name);
	}

	public static Color getBackground() {
		return currentScheme.get("background");
	}

	public static Color getSignal() {
		return currentScheme.get("signal");
	}

	public static Color getBusOn() {
		return currentScheme.get("bus-on");
	}

	public static Color getGates() {
		return currentScheme.get("gates");
	}

	public static Color getGround() {
		return currentScheme.get("ground");
	}

	public static Color getWireOn() {
		return currentScheme.get("wires-on");
	}

	public static Color getOff() {
		return currentScheme.get("off");
	}

	public static Color getColor(Pin pin) {
		return pin.getInValue() == Constants.THREE_STATE ? getGround()
				: pin.getInValue() == Constants.ON ? getWireOn() : getOff();
	}

	public static ColorScheme getScheme(String name) {
		return SCHEMES.get(name);
	}

	public static void add(String name) {
		SCHEMES.put(name, new ColorScheme(name));
		currentScheme = SCHEMES.get(name);
	}

	public String getName() {
		return name;
	}

	public static boolean exists(String name) {
		return SCHEMES.keySet().stream().anyMatch(schemeName -> schemeName.equalsIgnoreCase(name));
	}

	public static Color getLabel() {
		return currentScheme.get("label");
	}
}

