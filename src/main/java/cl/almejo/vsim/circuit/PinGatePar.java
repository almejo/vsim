/**
 *
 * vsim
 *
 * Created on Aug 15, 2013
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Gate;

public class PinGatePar {
	private final int _pinId;
	private final Gate _gate;

	public PinGatePar(int pinId, Gate gate) {
		_pinId = pinId;
		_gate = gate;
	}

	public int getPinId() {
		return _pinId;
	}

	public Gate getGate() {
		return _gate;
	}

	@Override
	public String toString() {
		return "(" + _pinId + ", " + _gate + ")";
	}
}
