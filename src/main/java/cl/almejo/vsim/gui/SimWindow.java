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

package cl.almejo.vsim.gui;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.CircuitCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SimWindow extends JFrame implements ComponentListener, WindowListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private final CircuitCanvas _canvas;
	private final Circuit _circuit;
	private int _xi;
	private int _yi;

	static final KeyStroke ACCELERATOR_UNDO = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_REDO = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);

	class RedoAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public RedoAction(String text) throws IOException {
			super(text, new ImageIcon(ImageIO.read(ClassLoader.getSystemResourceAsStream("icons/redo.png"))));
			setTitle(text);
			putValue(AbstractAction.SHORT_DESCRIPTION, text);
			putValue(AbstractAction.ACCELERATOR_KEY, ACCELERATOR_REDO);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			SimWindow.this.redo();
		}
	}

	class UndoAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public UndoAction(String text) throws IOException {
			super(text, new ImageIcon(ImageIO.read(ClassLoader.getSystemResourceAsStream("icons/undo.png"))));
			setTitle(text);
			putValue(AbstractAction.SHORT_DESCRIPTION, text);
			putValue(AbstractAction.ACCELERATOR_KEY, ACCELERATOR_UNDO);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			SimWindow.this.undo();
		}
	}

	public SimWindow(Circuit circuit) {
		_circuit = circuit;
		_canvas = new CircuitCanvas(_circuit);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().add(_canvas, BorderLayout.CENTER);
		setVisible(true);
		addComponentListener(this);
		_canvas.addMouseListener(this);
		_canvas.resizeViewport();
		JTextField text = new JTextField();
		getContentPane().add(text, BorderLayout.SOUTH);
		text.addKeyListener(this);

		JMenuBar menubar = new JMenuBar();
		JMenuItem menuItem = new JMenuItem();
		try {
			menuItem.setAction(new UndoAction("Undo"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		menubar.add(menuItem);

		menuItem = new JMenuItem();
		try {
			menuItem.setAction(new RedoAction("Do"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		menubar.add(menuItem);
		setJMenuBar(menubar);
	}

	protected void undo() {
		_circuit.undo();
	}

	protected void redo() {
		_circuit.redo();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		_canvas.resizeViewport();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		_circuit.remove(_canvas);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode());
		if (e.getKeyCode() == 10) {
			JTextField field = (JTextField) e.getSource();
			String[] points = field.getText().split(",");
			if (points.length < 4) {
				return;
			}
			System.out.println(Circuit.gridTrunc(Integer.parseInt(points[0].trim())) + ", "
					+ Circuit.gridTrunc(Integer.parseInt(points[1].trim())) + ", "
					+ Circuit.gridTrunc(Integer.parseInt(points[2].trim())) + ", "
					+ Circuit.gridTrunc(Integer.parseInt(points[3].trim())));
			_circuit.undoableConnect(Integer.parseInt(points[0].trim()), Integer.parseInt(points[1].trim()), Integer.parseInt(points[2].trim()), Integer.parseInt(points[3].trim()));
			field.setText("");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		_xi = e.getX();
		_yi = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		_circuit.undoableConnect(_xi, _yi, e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
