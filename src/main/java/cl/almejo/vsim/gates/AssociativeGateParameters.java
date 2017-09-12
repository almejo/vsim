package cl.almejo.vsim.gates;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class AssociativeGateParameters extends GateParametersWithDelay {
	private final int[][] _behavior;
	private final int _pinCount;
	private final int _associativeType;

	AssociativeGateParameters(int delay, int pinCount, int[][] behavior, int associativeType) {
		super(delay);
		_pinCount = pinCount;
		_behavior = behavior;
		_associativeType = associativeType;
	}

	int[][] getBehavior() {
		return _behavior;
	}

	public int getPinCount() {
		return _pinCount;
	}

	int getAssociativeType() {
		return _associativeType;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
