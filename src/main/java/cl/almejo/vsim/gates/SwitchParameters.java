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

	public void getValues(Map<String, Object> parameters) {
		super.getValues(parameters);
		parameters.put("value", _value);
	}


}
