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
package cl.almejo.vsim.gates;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.ConfigVariable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SwitchParameters extends GateParameters {
	private byte _value;
	private String _text;
	private static final String[] LABELS = new String[]{Messages.t("config.value.on.label"), Messages.t("config.value.off.label"), Messages.t("config.value.no.signal.label")};
	private static final String[] VALUES = new String[]{"" + Constants.ON, "" + Constants.OFF, "" + Constants.THREE_STATE};

	public SwitchParameters(byte value) {
		_value = value;
	}

	public byte getValue() {
		return _value;
	}

	public void setValues(Map<String, Object> parameters) {
		if ( parameters.get("value") instanceof String) {
			_value = Byte.parseByte((String) parameters.get("value"));
		}else if ( parameters.get("value") instanceof Integer) {
			_value = ((Integer) parameters.get("value")).byteValue();
		}
		_text = (String) parameters.get("text");
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<ConfigVariable>();
		variables.add(new ConfigVariable("text", Messages.t("config.text.label"), _text));
		variables.add(new ConfigVariable("value", Messages.t("config.value.label"), "" + _value, LABELS, VALUES));
		return variables;
	}

	public String getText() {
		return _text;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
