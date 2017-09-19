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

	private int number;

	private final int pinCount;

	SevenSegmentsDisplayParameters(int pinCount) {
		this.pinCount = pinCount;
	}


	void updateNumber(SevenSegmentsDisplay gate) {
		number = IntStream.range(0, gate.getPinCount())
				.filter(pinId -> gate.getPin(pinId).getInValue() > 0)
				.map(pinId -> (int) Math.pow(2, pinId)).sum();
	}

	int getNumber() {
		return number;
	}

	public int getPinCount() {
		return pinCount;
	}
}
