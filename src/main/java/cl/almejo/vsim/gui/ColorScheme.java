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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ColorScheme {

	private Color _background = Color.GRAY;

	private Color _ground = Color.GREEN;

	private Color _off = Color.BLACK;

	private Color _wireOn = Color.RED;

	private Color _busOn = Color.RED;

	private Color _gates = Color.BLUE;

	private static ColorScheme _current;

	private static HashMap<String, ColorScheme> _schemes = new HashMap<String, ColorScheme>();

	static {

		File file = new File("colors.json");
		try {
			String json = FileUtils.readFileToString(file);

			Map<String, Map<String, String>> schemes = (Map<String, Map<String, String>>) new ObjectMapper().readValue(json, Map.class);
			for (String name : schemes.keySet()) {
				Map<String, String> map = schemes.get(name);
				ColorScheme scheme = new ColorScheme();
				scheme.setBackground(new Color(Integer.parseInt(map.get("background").replace("#", ""), 16), false));
				scheme.setBusOn(new Color(Integer.parseInt(map.get("bus-on").replace("#", ""), 16), false));
				scheme.setGates(new Color(Integer.parseInt(map.get("gates").replace("#", ""), 16), false));
				scheme.setGround(new Color(Integer.parseInt(map.get("ground").replace("#", ""), 16), false));
				scheme.setOff(new Color(Integer.parseInt(map.get("off").replace("#", ""), 16), false));
				scheme.setWireOn(new Color(Integer.parseInt(map.get("wires-on").replace("#", ""), 16), false));
				_schemes.put(name, scheme);
			}
			_current = _schemes.get("default");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Color getBackground() {
		return _current._background;
	}

	public static Color getBusOn() {
		return _current._busOn;
	}

	public static Color getGates() {
		return _current._gates;
	}

	public static Color getGround() {
		return _current._ground;
	}

	public static Color getWireOn() {
		return _current._wireOn;
	}

	public static Color getOff() {
		return _current._off;
	}

	public static Color getColor(Pin pin) {
		return pin.getInValue() == Constants.THREE_STATE ? getGround()
				: pin.getInValue() == Constants.ON ? getWireOn() : getOff();
	}

	private void setBackground(Color background) {
		_background = background;
	}

	private void setBusOn(Color busOn) {
		_busOn = busOn;
	}

	private void setGates(Color gates) {
		_gates = gates;
	}

	private void setGround(Color ground) {
		_ground = ground;
	}

	private void setOff(Color off) {
		_off = off;
	}

	private void setWireOn(Color wireOn) {
		_wireOn = wireOn;
	}

	public static Component getCombox() {
		JComboBox<String> comboBox = new JComboBox<String>(_schemes.keySet().toArray(new String[_schemes.keySet().size()]));
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox jcmbType = (JComboBox) e.getSource();
				String cmbType = (String) jcmbType.getSelectedItem();
				_current = _schemes.get(cmbType);
			}
		});
		comboBox.setSelectedItem("default");
		return comboBox;
	}
}

