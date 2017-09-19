package cl.almejo.vsim.circuit;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private String[] values;
	private String[] labels;
	private final String label;
	private final String value;
	private final String name;
	private int step = 1;
	private final ConfigValueType type;

	public ConfigVariable(String name, String label, String value, ConfigValueType type) {
		this.label = label;
		this.value = value;
		this.type = type;
		this.name = name;
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
		this.labels = labels;
		this.values = values;
	}

	public static Map<String, Object> toMap(List<ConfigVariable> variables) {
		Map<String, Object> parameters = new HashMap<>();
		for (ConfigVariable variable : variables) {
			switch (variable.getType()) {
				case STRING:
				case LIST:
					parameters.put(variable.getName(), variable.getValue());
					break;
				case INT:
					parameters.put(variable.getName(), Integer.parseInt(variable.getValue()));
					break;
				case BYTE:
					parameters.put(variable.getName(), Byte.parseByte(variable.getValue()));
					break;
			}
		}
		return parameters;
	}

	public ConfigVariable setStep(int step) {
		this.step = step;
		return this;
	}
}
