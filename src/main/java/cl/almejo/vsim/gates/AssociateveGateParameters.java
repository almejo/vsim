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

public class AssociateveGateParameters extends GateParametersWithDelay {
	private int[][] _behavior;
	private int _pinCount;
	private int _associativeType;

	public AssociateveGateParameters(int delay, int pinCount, int[][] behavior, int associativeType) {
		super(delay);
		_pinCount = pinCount;
		_behavior = behavior;
		_associativeType = associativeType;
	}

	public int[][] getBehavior() {
		return _behavior;
	}

	public int getPinCount() {
		return _pinCount;
	}

	public int getAssociativeType() {
		return _associativeType;
	}
}
