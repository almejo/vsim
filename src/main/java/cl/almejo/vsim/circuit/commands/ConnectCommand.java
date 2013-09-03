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

import java.util.List;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Connection;
import cl.almejo.vsim.circuit.Contact;

public class ConnectCommand implements Command {

	private Circuit _circuit;
	private List<Connection<Contact>> _connections;
	private int _xi;
	private int _yi;
	private int _xf;
	private int _yf;

	public ConnectCommand(Circuit circuit, int xi, int yi, int xf, int yf) {
		_circuit = circuit;
		_connections = circuit.findBeforeConnect(xi, yi, xf, yf);
		_xi = xi;
		_yi = yi;
		_xf = xf;
		_yf = yf;
	}

	@Override
	public void apply() {
		_circuit.connect(_xi, _yi, _xf, _yf);
	}

	@Override
	public void unDo() {
		_circuit.disconnectBetween(_xi, _yi, _xf, _yf);
		if (_connections.size() > 0) {
			for (Connection<Contact> connection : _connections) {
				_circuit.connect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(),
					connection.getLast().getY());
			}
		}
	}
}
