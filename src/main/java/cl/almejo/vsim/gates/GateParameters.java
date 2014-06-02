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

abstract public class GateParameters implements Cloneable {

	protected int _delay;

	public GateParameters(int delay) {
		_delay = delay;
	}

	public int getDelay() {
		return _delay;
	}

	public void setValues(Map<String, Object> parameters) {
		_delay = (Integer) parameters.get("delay");
	}

	public void getValues(Map<String, Object> parameters) {
		parameters.put("delay", _delay);
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
