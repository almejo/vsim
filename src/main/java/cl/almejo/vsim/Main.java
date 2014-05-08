/**
 *
 * vsim
 *
 * Created on Aug 12, 2013
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;

public class Main {

	public Main() {
		Circuit circuit = new Circuit();

		circuit.undoableAddGate(GateFactory.getInstance(Gate.CLOCK, circuit), 100, 80);

		circuit.undoableAddGate(GateFactory.getInstance(Gate.CLOCK, circuit), 100, 118);

		circuit.undoableAddGate(GateFactory.getInstance(Gate.AND2, circuit), 300, 96);
		circuit.undoableAddGate(GateFactory.getInstance(Gate.NOT, circuit), 400, 96);

		circuit.undoableConnect(112, 96, 300, 96);
		circuit.undoableConnect(112, 128, 300, 128);
		circuit.undoableConnect(332, 112, 400, 112);

		circuit.undoableConnect(300, 200, 50, 200);
		circuit.undoableConnect(350, 200, 450, 200);
		circuit.undoableConnect(100, 200, 500, 200);

		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println("Unable to load native look and feel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		new SimWindow(circuit);
		new SimWindow(circuit);
	}

	public static void main(String[] args) {
		new Main();
	}
}
