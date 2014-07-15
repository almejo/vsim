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

public class GateData {
	private final GateDescriptor _descriptor;
	private final GateParameters _parameters;
	private final int[][] _connections;

	public GateData(GateDescriptor descriptor, GateParameters parameters, int pinCount) {
		_descriptor = descriptor;
		_parameters = parameters;
		_connections = new int[pinCount][2];
	}

	public void setGateAndPin(int pinId, int gateIndex, int pinIndex) {
		_connections[pinId][0] = gateIndex;
		_connections[pinId][1] = pinIndex;
	}

	@Override
	public String toString() {
		String ret = "";
		for (int[] _connection : _connections) {
			ret += _connection[0] + " -> " + _connection[1] + ":";
		}
		return ret;
	}
}
