package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Connection;
import cl.almejo.vsim.circuit.Contact;

import java.util.LinkedList;
import java.util.List;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class ConnectCommand implements Command {
	private class UndoableConnection {
		private List<Connection<Contact>> _previousConnections;
		private final int _xi;
		private final int _yi;
		private final int _xf;
		private final int _yf;

		UndoableConnection(int xi, int yi, int xf, int yf) {
			_xi = xi;
			_yi = yi;
			_xf = xf;
			_yf = yf;
		}

		void apply() {
			_previousConnections = _circuit.findBeforeConnect(_xi, _yi, _xf, _yf);
			_circuit.connect(_xi, _yi, _xf, _yf);
		}

		void undo() {
			_circuit.disconnectBetween(_xi, _yi, _xf, _yf);
			if (!_previousConnections.isEmpty()) {
				_previousConnections.forEach(connection -> _circuit.connect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(), connection.getLast().getY()));
			}
		}
	}

	private final Circuit _circuit;

	private final List<UndoableConnection> _undoableConnections;

	public ConnectCommand(Circuit circuit) {
		_circuit = circuit;
		_undoableConnections = new LinkedList<>();
	}

	public void connect(int xi, int yi, int xf, int yf) {
		_undoableConnections.add(new UndoableConnection(xi, yi, xf, yf));
	}

	@Override
	public boolean apply() {
		_undoableConnections.forEach(UndoableConnection::apply);
		return true;
	}

	@Override
	public void unDo() {
		_undoableConnections.forEach(UndoableConnection::undo);
	}
}
