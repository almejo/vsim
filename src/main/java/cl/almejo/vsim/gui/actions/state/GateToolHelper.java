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
public class GateToolHelper extends ActionToolHelper {
	private final int _gateIndex;

	public GateToolHelper(int gateIndex) {
		_gateIndex = gateIndex;
	}

	@Override
	public void mouseClicked(SimWindow window, MouseEvent event) {
		super.mouseClicked(window, event);
		if (event.isControlDown()) {
			window.getCircuit().undoableRemoveGate(event.getX(), event.getY());
			return;
		}
		window.getCircuit().undoableAddGate(GateFactory.getInstance(_gateIndex, window.getCircuit()), event.getX(), event.getY());
	}


}
