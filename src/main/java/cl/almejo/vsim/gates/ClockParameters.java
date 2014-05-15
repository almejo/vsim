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

public class ClockParameters extends GateParameters {

	private long _timeUp;

	private long _timeDown;

	private byte _state = Constants.OFF;

	public ClockParameters(long timeDown, long timeUp) {
		super(1);
		_timeDown = timeDown;
		_timeUp = timeUp;
	}

	public void setValues(Map<String , Object> parameters) {
		super.setValues(parameters);
		_timeDown = (Integer) parameters.get("time-down");
		_timeUp = (Integer) parameters.get("time-up");
	}

	public void getValues(Map<String, Object> parameters) {
		super.getValues(parameters);
		parameters.put("time-down", _timeDown);
		parameters.put("time-up", _timeUp);
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
