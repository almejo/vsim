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

	public SwitchParameters(byte value) {
		_value = value;
	}

	public byte getValue() {
		return _value;
	}


	public void setValues(Map<String, Object> parameters) {
		_value = ((Integer) parameters.get("value")).byteValue();
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<ConfigVariable>();
		variables.add(new ConfigVariable("value", Messages.t("config.value.label"), _value));
		return variables;
	}
}
