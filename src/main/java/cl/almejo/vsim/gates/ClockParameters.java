package cl.almejo.vsim.gates;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.ConfigVariable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class ClockParameters extends GateParameters {

	private int _timeUp;

	private int _timeDown;

	private byte _state = Constants.OFF;

	ClockParameters(int timeDown, int timeUp) {
		_timeDown = timeDown;
		_timeUp = timeUp;
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		_timeDown = (Integer) parameters.get("time-down");
		_timeUp = (Integer) parameters.get("time-up");
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<>();
		variables.add(new ConfigVariable("time-down", Messages.t("config.time.down.label"), _timeDown).setStep(1000));
		variables.add(new ConfigVariable("time-up", Messages.t("config.time.up.label"), _timeUp).setStep(1000));
		return variables;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}

	long getTimeUp() {
		return _timeUp;
	}

	long getTimeDown() {
		return _timeDown;
	}

	public byte getState() {
		return _state;
	}

	public void setState(byte b) {
		_state = b;
	}
}
