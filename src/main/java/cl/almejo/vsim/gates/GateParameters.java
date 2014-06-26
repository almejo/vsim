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
	public void setValues(Map<String, Object> parameters) {
	}

	public List<ConfigVariable> getValues() {
		return new LinkedList<ConfigVariable>();
	}
	
	public GateParameters clone() throws CloneNotSupportedException {
		try {
			return (GateParameters) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isConfigurable() {
		return false;
	}
}
