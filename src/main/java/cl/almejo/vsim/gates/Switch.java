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

import cl.almejo.vsim.circuit.Circuit;

public class Switch extends Gate {

	public Switch(Circuit circuit, GateParameters parameters, SwitchDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		_pins = new SimplePin[descriptor.getPinCount()];
		_pins[0] = new SimplePin(this, circuit.getScheduler(), 0);
		_pins[0].program(((SwitchParameters) parameters).getValue(), getParamameters().getDelay());
		_pins[0].hasChanged();
	}

	@Override
	public void parametersUpdated() {
		super.parametersUpdated();
		_pins[0].program(((SwitchParameters) getParamameters()).getValue(), getParamameters().getDelay());
		_pins[0].hasChanged();
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
