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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SevenSegmentsDisplayParameters extends GateParameters {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SevenSegmentsDisplayParameters.class);

	private int _number = 0;

	private int _pinCount = 8;

	public SevenSegmentsDisplayParameters(int delay) {
		super(delay);
	}


	public void updateNumber(SevenSegmentsDisplay gate) {
		int value = 0;
		for (int pinId = 0; pinId < gate.getPinCount(); pinId++) {
			if (gate.getPin(pinId).getInValue() > 0) {
				value += Math.pow(2, pinId);
			}
		}

		_number = value;
		LOGGER.debug("SevenSegmentsUpdated: " + value);
	}

	public int getNumber() {
		return _number;
	}

	public int getPinCount() {
		return _pinCount;
	}
}
