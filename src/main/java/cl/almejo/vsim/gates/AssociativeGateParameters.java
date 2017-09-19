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
	private final int[][] behavior;
	private final int pinCount;
	private final int associativeType;

	AssociativeGateParameters(int delay, int pinCount, int[][] behavior, int associativeType) {
		super(delay);
		this.pinCount = pinCount;
		this.behavior = behavior;
		this.associativeType = associativeType;
	}

	int[][] getBehavior() {
		return behavior;
	}

	public int getPinCount() {
		return pinCount;
	}

	int getAssociativeType() {
		return associativeType;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
