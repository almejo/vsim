package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Gate;
import lombok.Getter;
/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
@Getter
class PinGatePar {
	private final int _pinId;
	private final Gate _gate;

	PinGatePar(int pinId, Gate gate) {
		_pinId = pinId;
		_gate = gate;
	}

	@Override
	public String toString() {
		return "(" + _pinId + ", " + _gate + ")";
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PinGatePar)) {
			return super.equals(object);
		}
		PinGatePar par = (PinGatePar) object;
		return par.getPinId() == _pinId && par.getGate() == _gate;
	}
}
