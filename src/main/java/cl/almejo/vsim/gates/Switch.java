package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class Switch extends Gate {

	Switch(Circuit circuit, GateParameters parameters, SwitchDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		pins = new SimplePin[descriptor.getPinCount()];
		pins[0] = new SimplePin(this, circuit.getScheduler(), 0);
		pins[0].program(((SwitchParameters) parameters).getValue(), 1);
		pins[0].hasChanged();
	}

	@Override
	public void parametersUpdated() {
		super.parametersUpdated();
		pins[0].program(((SwitchParameters) getParamameters()).getValue(), 1);
		pins[0].hasChanged();
	}

}
