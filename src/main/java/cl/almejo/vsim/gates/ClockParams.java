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

public class ClockParams extends GateParameters {

	private long _timeUp;

	private long _timeDown;

	private byte _state = Constants.OFF;

	public ClockParams(long timeDown, long timeUp) {
		super(0);
		_timeDown = timeDown;
		_timeUp = timeUp;
	}

	public long getTimeUp() {
		return _timeUp;
	}

	public long getTimeDown() {
		return _timeDown;
	}

	public byte getState() {
		return _state;
	}

	public void setState(byte b) {
		_state = b;
	}
}
