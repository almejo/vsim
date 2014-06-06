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


import cl.almejo.vsim.gui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;

public class TimeDiagramDisplay extends JPanel implements DisplayPanel {

	private final TimeDiagramCanvas _diagramCanvas;
	private static final int SIGNAL_HEIGHT = 20;
	private static final int SPACE_BETWEEN_SIGNALS = 10;
	private static final int SIGNAL_HEIGHT_HALF = SIGNAL_HEIGHT / 2;

	static class TimeDiagramCanvas extends JPanel implements ComponentListener {
		private byte[][] _values = new byte[4][1000];

		private int _position = 0;
		TimeDiagramCanvas() {
			setBorder(new EmptyBorder(3,3,3,3));
			addComponentListener(this);
			setBackground(ColorScheme.getBackground());
			for (byte[] _value : _values) {
				Arrays.fill(_value, Constants.THREE_STATE);
			}
			updateValues();
		}

		@Override
		public void paint(Graphics graphics) {
			super.paint(graphics);
			Graphics2D graphics2D = (Graphics2D) graphics;
			for (int i = 0; i < _values[0].length; i++) {
				drawInstant(new byte[]{_values[0][i], _values[1][i], _values[2][i], _values[3][i]}, graphics2D, i);
			}
			graphics2D.setColor(ColorScheme.getGrid());
			graphics2D.drawLine(0, (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4+ SPACE_BETWEEN_SIGNALS, getWidth(), (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4+ SPACE_BETWEEN_SIGNALS);
			graphics2D.setColor(ColorScheme.getLabel());
		}

		public void plot(byte[] values) {
			if (_values[0].length < 1) {
				return;
			}
			if (_position >= _values[0].length) {
				_position = 0;
			}
			for (int i = 0; i < values.length; i++) {
				_values[i][_position] = values[i];
			}
			if (!isVisible()) {
				return;
			}
			Graphics2D graphics = (Graphics2D) getGraphics();
			graphics.setColor(ColorScheme.getBackground());
			graphics.fillRect(_position, 0, 20, (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4);
			drawInstant(values, graphics, _position);
			_position++;
		}

		private void drawInstant(byte[] values, Graphics2D graphics, int positionInstance) {
			int delta = SIGNAL_HEIGHT + 10;
			int index = 0;
			for (byte value : values) {
				graphics.setColor(ColorScheme.getSignal());
				switch (value) {
					case Constants.ON:
						graphics.drawLine(positionInstance, delta - SIGNAL_HEIGHT, positionInstance + 1, delta - SIGNAL_HEIGHT);
						break;
					case Constants.OFF:
						graphics.drawLine(positionInstance, delta, positionInstance + 1, delta);
						break;
					default:
						graphics.setColor(ColorScheme.getGround());
						graphics.drawLine(positionInstance, delta - SIGNAL_HEIGHT_HALF, positionInstance + 1, delta - SIGNAL_HEIGHT_HALF);
						break;
				}

				if (positionInstance > 0) {
					if (_values[index][positionInstance - 1] != Constants.THREE_STATE && _values[index][positionInstance] != _values[index][positionInstance - 1]) {
						graphics.drawLine(_position, delta - SIGNAL_HEIGHT, positionInstance, delta);
					}
				}
				delta += SPACE_BETWEEN_SIGNALS + SIGNAL_HEIGHT;
				index++;
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			updateValues();
		}

		private void updateValues() {
			byte[][] values = new byte[4][getWidth()];
			System.out.println(getWidth());
			for (int i = 0; i < _values.length; i++) {
				Arrays.fill(values[i], Constants.THREE_STATE);
				System.arraycopy(_values[i], 0, values[i], 0, Math.min(getWidth(), _values[i].length));
			}
			_values = values;
			if (_position >= getWidth()) {
				_position = 0;
			}
			repaint();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			updateValues();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			updateValues();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			updateValues();
		}
	}

	public TimeDiagramDisplay() {
		setLayout(new BorderLayout());
		_diagramCanvas = new TimeDiagramCanvas();
		add(BorderLayout.CENTER, _diagramCanvas);
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		buttons.setBorder(new EmptyBorder(3,3,3,3));
		buttons.add(new JButton("save"));
		buttons.add(new JButton("print"));
		add(BorderLayout.LINE_END, buttons);
	}

	public void plot(byte[] values) {
		_diagramCanvas.plot(values);
	}
}
