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
import cl.almejo.vsim.simulation.Scheduler;
import cl.almejo.vsim.simulation.SimulationEvent;

import javax.swing.*;

public class TimeDiagram extends Gate implements DisplayInfoGate {

	private final PlotEvent _plotEvent;

	private TimeDiagramDisplay _timeDiagramCanvas;

	class PlotEvent extends SimulationEvent {

		private final TimeDiagram _timeDiagram;

		public PlotEvent(Scheduler scheduler, TimeDiagram timeDiagram) {
			super(scheduler);
			_timeDiagram = timeDiagram;
		}

		@Override
		public void happen() {
			_timeDiagram.plot();
			schedule(100);
		}
	}

	public TimeDiagram(Circuit circuit, GateParameters parameters, TimeDiagramDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		_pins = new Pin[descriptor.getPinCount()];
		for (int pindId = 0; pindId < _pins.length; pindId++) {
			_pins[pindId] = new SimplePin(this, circuit.getScheduler(), pindId);
		}
		_plotEvent = new PlotEvent(circuit.getScheduler(), this);
		_timeDiagramCanvas = new TimeDiagramDisplay();
	}

	private void plot() {
		if (_timeDiagramCanvas != null) {
			_timeDiagramCanvas.plot(new byte[]{_pins[0].getInValue()
					, _pins[1].getInValue()
					, _pins[2].getInValue()
					, _pins[3].getInValue()});
		}
	}

	@Override
	public JPanel getDisplay() {
		return _timeDiagramCanvas;
	}

	public void startDisplay() {
		_plotEvent.schedule(1);
	}
}
