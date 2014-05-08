/**
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.gates;

public class AssociateveGateParameters extends GateParameters {
	private  int[][] _behavior;
	private  int _pinCount;

	public AssociateveGateParameters(int delay, int pinCount, int[][] behavior) {
		super(delay);
		_pinCount = pinCount;
		_behavior = behavior;
	}

	public int[][] getBehavior() {
		return _behavior;
	}

	public int getPinCount(){
		return _pinCount;
	}
}
