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
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import cl.almejo.vsim.circuit.commands.CommandManager;
import cl.almejo.vsim.circuit.commands.ConnectCommand;

public class SimWindow extends JFrame implements ComponentListener, WindowListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private CircuitCanvas _canvas;
	private final Circuit _circuit;
	private JTextField _text;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(_canvas, BorderLayout.CENTER);
		setVisible(true);
		addComponentListener(this);
		_canvas.resizeViewport();
		_text = new JTextField();
		getContentPane().add(_text, BorderLayout.SOUTH);
		_text.addKeyListener(this);

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
}
