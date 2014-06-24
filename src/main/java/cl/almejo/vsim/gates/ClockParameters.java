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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClockParameters extends GateParameters {

	private int _timeUp;

	private int _timeDown;

	private byte _state = Constants.OFF;

	public ClockParameters(int timeDown, int timeUp) {
		super(1);
		_timeDown = timeDown;
		_timeUp = timeUp;
	}

	public void setValues(Map<String, Object> parameters) {
		super.setValues(parameters);
		_timeDown = (Integer) parameters.get("time-down");
		_timeUp = (Integer) parameters.get("time-up");
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<ConfigVariable>();
		variables.add(new ConfigVariable("time-down", "Time down", _timeDown));
		variables.add(new ConfigVariable("time-up", "Time up", _timeUp));
		return variables;
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
