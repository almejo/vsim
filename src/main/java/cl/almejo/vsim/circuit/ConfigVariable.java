package cl.almejo.vsim.circuit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
@Getter
public class ConfigVariable {
	public final static int MAX = Integer.MAX_VALUE;
	public final static int MIN = 1;

	private String[] _values;
	private String[] _labels;
	private final String _label;
	private final String _value;
	private final String _name;
	private int _step = 1;
	private final ConfigValueType _type;

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

	public ConfigVariable(String name, String label, String value, String[] labels, String[] values) {
		this(name, label, value, ConfigValueType.LIST);
		_labels = labels;
		_values = values;
	}

	public static Map<String, Object> toMap(List<ConfigVariable> variables) {
		Map<String, Object> parameters = new HashMap<>();
		for (ConfigVariable variable : variables) {
			if (variable.getType() == ConfigValueType.STRING || variable.getType() == ConfigValueType.LIST) {
				parameters.put(variable.getName(), variable.getValue());
			} else if (variable.getType() == ConfigValueType.INT) {
				parameters.put(variable.getName(), Integer.parseInt(variable.getValue()));
			} else if (variable.getType() == ConfigValueType.BYTE) {
				parameters.put(variable.getName(), Byte.parseByte(variable.getValue()));
			}
		}
		return parameters;
	}

	public ConfigVariable setStep(int step) {
		_step = step;
		return this;
	}
}
