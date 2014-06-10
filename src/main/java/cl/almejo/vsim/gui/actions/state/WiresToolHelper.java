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

package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.circuit.CircuitCanvas;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

public class WiresToolHelper extends ActionToolHelper {
	private int _xi;
	private int _yi;
	private boolean _connecting;

	@Override
	public void mouseDown(SimWindow window, MouseEvent event) {
		_xi = event.getX();
		_yi = event.getY();
		window.getCircuit().setDrawConnectPreview(true);

		CircuitCanvas canvas = window.getCanvas();
		window.getCircuit().setConnectPreview(canvas.toCircuitCoordinatesX(_xi)
				, canvas.toCircuitCoordinatesY(_yi)
				, canvas.toCircuitCoordinatesX(_xi)
				, canvas.toCircuitCoordinatesY(_yi));
		_connecting = true;
	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		if (event.getClickCount() < 1) {
			CircuitCanvas canvas = window.getCanvas();
			window.undoableConnect(canvas.toCircuitCoordinatesX(_xi)
					, canvas.toCircuitCoordinatesY(_yi)
					, canvas.toCircuitCoordinatesX(event.getX())
					, canvas.toCircuitCoordinatesY(event.getY()));
			_connecting = false;
		}
		window.getCircuit().setDrawConnectPreview(false);

	}

	@Override
	public void mouseDoubleClicked(SimWindow window, MouseEvent event) {
		if (event.isControlDown()) {
			CircuitCanvas canvas = window.getCanvas();
			window.undoableDisconnect(canvas.toCircuitCoordinatesX(event.getX()), canvas.toCircuitCoordinatesY(event.getY()));
		}
		window.getCircuit().setDrawConnectPreview(false);
	}

	public void mouseDragged(SimWindow window, MouseEvent event) {
		if (_connecting) {
			CircuitCanvas canvas = window.getCanvas();
			window.getCircuit().setConnectPreview(canvas.toCircuitCoordinatesX(_xi)
					, canvas.toCircuitCoordinatesY(_yi)
					, canvas.toCircuitCoordinatesX(event.getX())
					, canvas.toCircuitCoordinatesY(event.getY()));
		}
	}
}
