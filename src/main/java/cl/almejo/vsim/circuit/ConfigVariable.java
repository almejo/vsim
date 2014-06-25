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

package cl.almejo.vsim.circuit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigVariable {
	private String _label;
	private String _value;
	private String _name;
	private ConfigValueType _type;

	public ConfigVariable(String name, String label, String value, ConfigValueType type) {
		_label = label;
		_value = value;
		_type = type;
		_name = name;
	}

	public ConfigVariable(String name, String label, int value) {
		this(name, label, "" + value, ConfigValueType.INT);
	}

	public ConfigVariable(String name, String label, byte value) {
		this(name, label, "" + value, ConfigValueType.BYTE);
	}

	public ConfigVariable(String name, String label, String value) {
		this(name, label, value, ConfigValueType.STRING);
	}

	public String getLabel() {
		return _label;
	}

	public String getValue() {
		return _value;
	}

	public ConfigValueType getType() {
		return _type;
	}

	public String getName() {
		return _name;
	}

	public static Map<String, Object> toMap(List<ConfigVariable> variables) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (ConfigVariable variable : variables) {
			if (variable.getType() == ConfigValueType.STRING) {
				parameters.put(variable.getName(), variable.getValue());
			} else if (variable.getType() == ConfigValueType.INT) {
				parameters.put(variable.getName(), Integer.parseInt(variable.getValue()));
			} else if (variable.getType() == ConfigValueType.BYTE) {
				parameters.put(variable.getName(), Byte.parseByte(variable.getValue()));
			}
		}
		return parameters;
	}
}
