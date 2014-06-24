package cl.almejo.vsim.circuit;

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
}
