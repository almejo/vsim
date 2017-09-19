package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.simulation.Scheduler;
import cl.almejo.vsim.simulation.SimulationEvent;

import javax.swing.*;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class TimeDiagram extends Gate implements DisplayInfoGate {

	private final PlotEvent plotEvent;

	private final TimeDiagramDisplay timeDiagramDisplay;

	private static class PlotEvent extends SimulationEvent {

		private final TimeDiagram timeDiagram;

		PlotEvent(Scheduler scheduler, TimeDiagram timeDiagram) {
			super(scheduler);
			this.timeDiagram = timeDiagram;
		}

		@Override
		public void happen() {
			timeDiagram.plot();
			schedule(150);
		}
	}

	TimeDiagram(Circuit circuit, GateParameters parameters, TimeDiagramDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		pins = new Pin[descriptor.getPinCount()];
		for (int pinId = 0; pinId < pins.length; pinId++) {
			pins[pinId] = new SimplePin(this, circuit.getScheduler(), pinId);
		}
		plotEvent = new PlotEvent(circuit.getScheduler(), this);
		timeDiagramDisplay = new TimeDiagramDisplay();
	}

	private void plot() {
		if (timeDiagramDisplay != null) {
			timeDiagramDisplay.plot(new byte[]{pins[0].getInValue()
					, pins[1].getInValue()
					, pins[2].getInValue()
					, pins[3].getInValue()});
		}
	}

	@Override
	public JPanel getDisplay() {
		return timeDiagramDisplay;
	}

	@Override
	public void startDisplay() {
		plotEvent.schedule(1);
	}
}
