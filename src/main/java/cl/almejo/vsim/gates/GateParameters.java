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

abstract public class GateParameters implements Cloneable {

	protected int _delay;

	public GateParameters(int delay) {
		_delay = delay;
	}

	public int getDelay() {
		return _delay;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
