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

public class SwitchParameters extends GateParameters {
	private byte _value;

	public SwitchParameters(byte value, int delay) {
		super(delay);
		_value = value;
	}

	public byte getValue() {
		return _value;
	}

}
