package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.circuit.CircuitCanvas;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class WiresToolHelper extends ActionToolHelper {
	private int xi;
	private int yi;
	private boolean connecting;

	@Override
	public void mouseDown(SimWindow window, MouseEvent event) {
		xi = event.getX();
		yi = event.getY();
		window.getCircuit().setDrawConnectPreview(true);

		CircuitCanvas canvas = window.getCanvas();
		window.getCircuit().setConnectPreview(canvas.toCircuitCoordinatesX(xi)
				, canvas.toCircuitCoordinatesY(yi)
				, canvas.toCircuitCoordinatesX(xi)
				, canvas.toCircuitCoordinatesY(yi));
		connecting = true;
	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		if (event.getClickCount() < 1) {
			CircuitCanvas canvas = window.getCanvas();
			window.undoableConnect(canvas.toCircuitCoordinatesX(xi)
					, canvas.toCircuitCoordinatesY(yi)
					, canvas.toCircuitCoordinatesX(event.getX())
					, canvas.toCircuitCoordinatesY(event.getY()));
			connecting = false;
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

	@Override
	public void mouseDragged(SimWindow window, MouseEvent event) {
		if (connecting) {
			CircuitCanvas canvas = window.getCanvas();
			window.getCircuit().setConnectPreview(canvas.toCircuitCoordinatesX(xi)
					, canvas.toCircuitCoordinatesY(yi)
					, canvas.toCircuitCoordinatesX(event.getX())
					, canvas.toCircuitCoordinatesY(event.getY()));
		}
	}
}
