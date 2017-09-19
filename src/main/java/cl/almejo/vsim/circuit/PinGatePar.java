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
	private final int pinId;
	private final Gate gate;

	PinGatePar(int pinId, Gate gate) {
		this.pinId = pinId;
		this.gate = gate;
	}

	@Override
	public String toString() {
		return "(" + pinId + ", " + gate + ")";
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PinGatePar)) {
			return super.equals(object);
		}
		PinGatePar par = (PinGatePar) object;
		return par.getPinId() == pinId && par.getGate() == gate;
	}
}
