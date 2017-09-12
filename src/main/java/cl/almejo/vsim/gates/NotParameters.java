package cl.almejo.vsim.gates;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class NotParameters extends GateParametersWithDelay {

	NotParameters(int delay) {
		super(delay);
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
