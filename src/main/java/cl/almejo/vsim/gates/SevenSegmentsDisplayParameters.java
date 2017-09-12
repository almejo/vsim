package cl.almejo.vsim.gates;

import java.util.stream.IntStream;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class SevenSegmentsDisplayParameters extends GateParameters {

	private int _number;

	private final int _pinCount;

	SevenSegmentsDisplayParameters(int pinCount) {
		_pinCount = pinCount;
	}


	void updateNumber(SevenSegmentsDisplay gate) {
		_number = IntStream.range(0, gate.getPinCount())
				.filter(pinId -> gate.getPin(pinId).getInValue() > 0)
				.map(pinId -> (int) Math.pow(2, pinId)).sum();
	}

	int getNumber() {
		return _number;
	}

	public int getPinCount() {
		return _pinCount;
	}
}
