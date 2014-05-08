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

package cl.almejo.vsim.circuit;

public class CircuitEvent {

	private final Circuit _circuit;

	public CircuitEvent(Circuit circuit) {
		_circuit = circuit;
	}


	public Circuit getCircuit() {
		return _circuit;
	}
}
