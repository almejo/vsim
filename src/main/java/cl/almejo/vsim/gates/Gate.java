/**
 *
 * vsim
 *
 * Created on Aug 1, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */
 
package cl.almejo.vsim.gates;


public class Gate {

	protected Pin[] _pins;
	
	protected GateParameters _parameters;
	
	public Gate(GateParameters params) {
		_parameters = params;
	}

	public GateParameters getParams() {
		return _parameters;
	}
	
	public Pin getPin(int i) {
		return _pins[i];
	}

}
