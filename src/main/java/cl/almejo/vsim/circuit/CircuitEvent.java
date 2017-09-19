package cl.almejo.vsim.circuit;

import lombok.Getter;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */

public class CircuitEvent {

	@Getter
	private final Circuit circuit;

	CircuitEvent(Circuit circuit) {
		this.circuit = circuit;
	}
}
