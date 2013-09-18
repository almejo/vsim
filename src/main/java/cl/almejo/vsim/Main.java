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
import cl.almejo.vsim.gates.*;
import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;

public class Main {

	public Main() {
		Circuit circuit = new Circuit();

		ClockDescriptor descriptor = new ClockDescriptor();
		IconGate iconClock = new IconGate(circuit.getNextGateId(), new Clock(circuit, new ClockParams(1000, 1000), descriptor));
		circuit.add(iconClock, 100, 80);

		IconGate iconClock2 = new IconGate(circuit.getNextGateId(), new Clock(circuit, new ClockParams(3000, 3000), descriptor));
		circuit.add(iconClock2, 100, 118);

		IconGate iconAnd = new IconGate(circuit.getNextGateId(), new And(circuit, new AndParams(1), new AndDescriptor()));
		circuit.add(iconAnd, 300, 96);

		IconGate iconNot = new IconGate(circuit.getNextGateId(), new Not(circuit, new NotParams(1), new NotDescriptor()));
		circuit.add(iconNot, 400, 96);

		circuit.undoableConnect(112, 96, 300, 96);
		circuit.undoableConnect(112, 128, 300, 128);
		circuit.undoableConnect(332, 112, 400, 112);
		System.out.println("================================================");
		circuit.undoableDisconnect(332, 112);

		circuit.undoableConnect(300, 200, 50, 200);
		circuit.undoableConnect(350, 200, 450, 200);
		circuit.undoableConnect(100, 200, 500, 200);

		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println("Unable to load native look and feel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (InstantiationException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IllegalAccessException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		new SimWindow(circuit);
		new SimWindow(circuit);
	}

	public static void main(String[] args) {
		new Main();
	}
}
