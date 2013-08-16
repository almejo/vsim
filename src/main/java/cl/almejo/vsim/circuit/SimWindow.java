/**
 *
 * vsim
 *
 * Created on Aug 16, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */
 
package cl.almejo.vsim.circuit;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class SimWindow {

	private JFrame _frame;
	private CircuitCanvas _canvas;
	private final Circuit _circuit; 

	public SimWindow(Circuit circuit) {
		_circuit = circuit;
		_canvas = new CircuitCanvas(_circuit);
		_frame = new JFrame();
		_frame.setBounds(100, 100, 450, 300);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.getContentPane().add(_canvas, BorderLayout.CENTER);
		_frame.setVisible(true);
	}

	
}
