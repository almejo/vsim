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

abstract public class GateParameters implements Cloneable {

	protected int _delay;

	public GateParameters(int delay) {
		_delay = delay;
	}

	public int getDelay() {
		return _delay;
	}

	public void setValues(Map<String, Object> parameters) {
		if (parameters.containsKey("delay")) {
			_delay = (Integer) parameters.get("delay");
		}
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<ConfigVariable>();
		variables.add(new ConfigVariable("delay", "Delay", _delay));
		return variables;
	}

	public GateParameters clone() throws CloneNotSupportedException {
		try {
			return (GateParameters) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
