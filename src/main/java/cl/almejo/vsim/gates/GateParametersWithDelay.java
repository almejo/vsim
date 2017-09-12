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
abstract class GateParametersWithDelay extends GateParameters {

	private int _delay;

	GateParametersWithDelay(int delay) {
		_delay = delay;
	}

	public int getDelay() {
		return _delay;
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		_delay = (Integer) parameters.get("delay");
	}

	@Override
	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<>();
		variables.add(new ConfigVariable("delay", Messages.t("config.delay.label"), _delay));
		return variables;
	}
}
