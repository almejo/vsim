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

public class SevenSegmentsDisplayParameters extends GateParameters {

	private int _number = 0;

	private final int _pinCount;

	public SevenSegmentsDisplayParameters(int pinCount) {
		_pinCount = pinCount;
	}


	public void updateNumber(SevenSegmentsDisplay gate) {
		int value = 0;
		for (int pinId = 0; pinId < gate.getPinCount(); pinId++) {
			if (gate.getPin(pinId).getInValue() > 0) {
				value += Math.pow(2, pinId);
			}
		}

		_number = value;
	}

	public int getNumber() {
		return _number;
	}

	public int getPinCount() {
		return _pinCount;
	}
}
