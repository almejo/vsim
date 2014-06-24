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

import cl.almejo.vsim.circuit.ConfigVariable;

import java.util.List;
import java.util.Map;

public class SwitchParameters extends GateParameters {
	private byte _value;

	public SwitchParameters(byte value, int delay) {
		super(delay);
		_value = value;
	}

	public byte getValue() {
		return _value;
	}


	public void setValues(Map<String, Object> parameters) {
		super.setValues(parameters);
		_value = ((Integer) parameters.get("value")).byteValue();
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = super.getValues();
		variables.add(new ConfigVariable("value", "Value", _value));
		return variables;
	}
}
