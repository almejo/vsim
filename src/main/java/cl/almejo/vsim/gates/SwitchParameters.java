package cl.almejo.vsim.gates;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.ConfigVariable;
import lombok.Getter;

import java.util.LinkedList;
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
class SwitchParameters extends GateParameters {
	private byte value;

	@Getter
	private String text;
	private static final String[] LABELS = new String[]{Messages.t("config.value.on.label"), Messages.t("config.value.off.label"), Messages.t("config.value.no.signal.label")};
	private static final String[] VALUES = new String[]{"" + Constants.ON, "" + Constants.OFF, "" + Constants.THREE_STATE};

	SwitchParameters(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		if (parameters.get("value") instanceof String) {
			value = Byte.parseByte((String) parameters.get("value"));
		} else if (parameters.get("value") instanceof Integer) {
			value = ((Integer) parameters.get("value")).byteValue();
		}
		text = (String) parameters.get("text");
	}

	@Override
	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<>();
		variables.add(new ConfigVariable("text", Messages.t("config.text.label"), text));
		variables.add(new ConfigVariable("value", Messages.t("config.value.label"), "" + value, LABELS, VALUES));
		return variables;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
