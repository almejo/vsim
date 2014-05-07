package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alejo
 * Date: 9/18/13
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddGateToolHelper extends ActionToolHelper {
	private final int _gateIndex;
	private IconGate _gate;

	public AddGateToolHelper(int gateIndex) {
		_gateIndex = gateIndex;
	}

	@Override
	public void mouseClicked(SimWindow window, MouseEvent event) {
		super.mouseClicked(window, event);
		_gate = GateFactory.getInstnce(_gateIndex, window.getCircuit());
		window.getCircuit().undoableAddGate(_gate, event.getX(), event.getY());
	}

	public void mouseDragged(SimWindow window, MouseEvent event) {
		window.getCircuit().setGatePreview(_gateIndex, event.getX(), event.getY());
	}
}
