/**
 *
 * vsim
 *
 * Created on Aug 25, 2013
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Connection;
import cl.almejo.vsim.circuit.Contact;

import java.util.LinkedList;
import java.util.List;

public class ConnectCommand implements Command {


	class UndoableConnection {
		private List<Connection<Contact>> _previousConnections;
		private int _xi;
		private int _yi;
		private int _xf;
		private int _yf;

		public UndoableConnection(int xi, int yi, int xf, int yf) {
			_xi = xi;
			_yi = yi;
			_xf = xf;
			_yf = yf;
		}

		void apply() {
			System.out.println("Creando conneccion");
			_previousConnections = _circuit.findBeforeConnect(_xi, _yi, _xf, _yf);
			for (Connection connection: _previousConnections) {
				System.out.println("..." + connection);
			}
			System.out.println("==>" + _xi + ", " + _yi + ", "+ _xf + ", "+_yf);
			_circuit.connect(_xi, _yi, _xf, _yf);
		}

		void undo() {
			_circuit.disconnectBetween(_xi, _yi, _xf, _yf);
			if (_previousConnections.size() > 0) {
				for (Connection<Contact> connection : _previousConnections) {
					_circuit.connect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(),
							connection.getLast().getY());
				}
			}
		}
	}

	private Circuit _circuit;

	private List<UndoableConnection> _undoableConnections;

	public ConnectCommand(Circuit circuit) {
		_circuit = circuit;
		_undoableConnections = new LinkedList<UndoableConnection>();
	}

	public void connect(int xi, int yi, int xf, int yf) {
		_undoableConnections.add(new UndoableConnection(xi, yi, xf, yf));
	}

	@Override
	public void apply() {
		for (UndoableConnection connection : _undoableConnections) {
			connection.apply();
		}
	}

	@Override
	public void unDo() {
		for (UndoableConnection connection : _undoableConnections) {
			connection.undo();
		}
	}
}
