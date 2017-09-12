package cl.almejo.vsim.gui;

import cl.almejo.vsim.circuit.CircuitCanvas;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public interface ViewportListener {
	void updated(CircuitCanvas circuitCanvas);
}
