package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alejo
 * Date: 9/18/13
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class WiresToolHelper extends ActionToolHelper {
	private int _xi;
	private int _yi;
	private boolean _connecting;

	@Override
	public void mouseDown(SimWindow window, MouseEvent event) {
		_xi = event.getX();
		_yi = event.getY();
		window.setDrawConnectPreview(true);
		window.setConnectPreview(_xi, _yi, _xi, _yi);
		_connecting = true;
	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		if (event.getClickCount() < 1) {
			window.undoableConnect(_xi, _yi, event.getX(), event.getY());
			_connecting = false;
		}
		window.setDrawConnectPreview(false);

	}

	@Override
	public void mouseDoubleClicked(SimWindow window, MouseEvent event) {
		if (event.isControlDown()) {
			window.undoableDisconnect(event.getX(), event.getY());
		}
		window.setDrawConnectPreview(false);
	}

	public void mouseDragged(SimWindow window, MouseEvent event) {
		if (_connecting) {
			window.setConnectPreview(_xi
					, _yi
					, Circuit.gridTrunc(event.getX())
					, Circuit.gridTrunc(event.getY()));
		}
	}
}
