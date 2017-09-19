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

	private int delay;

	GateParametersWithDelay(int delay) {
		this.delay = delay;
	}

	int getDelay() {
		return delay;
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		delay = (Integer) parameters.get("delay");
	}

	@Override
	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<>();
		variables.add(new ConfigVariable("delay", Messages.t("config.delay.label"), delay));
		return variables;
	}
}
