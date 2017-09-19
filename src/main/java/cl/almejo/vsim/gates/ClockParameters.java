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

	private int timeUp;

	private int timeDown;

	private byte state = Constants.OFF;

	ClockParameters(int timeDown, int timeUp) {
		this.timeDown = timeDown;
		this.timeUp = timeUp;
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		timeDown = (Integer) parameters.get("time-down");
		timeUp = (Integer) parameters.get("time-up");
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<>();
		variables.add(new ConfigVariable("time-down", Messages.t("config.time.down.label"), timeDown).setStep(1000));
		variables.add(new ConfigVariable("time-up", Messages.t("config.time.up.label"), timeUp).setStep(1000));
		return variables;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}

	long getTimeUp() {
		return timeUp;
	}

	long getTimeDown() {
		return timeDown;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte b) {
		state = b;
	}
}
