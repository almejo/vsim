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


import cl.almejo.vsim.Messages;
import cl.almejo.vsim.gui.ColorScheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class TimeDiagramDisplay extends JPanel implements DisplayPanel, ComponentListener {

	private final TimeDiagramCanvas _diagramCanvas;
	private static final int SIGNAL_HEIGHT = 20;
	private static final int SPACE_BETWEEN_SIGNALS = 10;
	private static final int SIGNAL_HEIGHT_HALF = SIGNAL_HEIGHT / 2;
	private static final int TOTAL_HEIGHT = (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4;

	static class TimeDiagramCanvas extends JPanel {
		private byte[][] _values = new byte[4][1000];
		private int _position = 0;

		TimeDiagramCanvas() {
			setBorder(new EmptyBorder(3, 3, 3, 3));
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
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for (int i = 0; i < _values[0].length; i++) {
				drawInstant(new byte[]{_values[0][i], _values[1][i], _values[2][i], _values[3][i]}, graphics2D, i);
			}
			graphics2D.setColor(ColorScheme.getGrid());
			graphics2D.drawLine(0, (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4 + SPACE_BETWEEN_SIGNALS, getWidth(), (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4 + SPACE_BETWEEN_SIGNALS);
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
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(ColorScheme.getBackground());
			graphics.fillRect(_position, 0, 20, TOTAL_HEIGHT + 6);

			drawInstant(values, graphics, _position);

			drawPlotter(graphics, values);
			_position++;
		}

		private void drawPlotter(Graphics2D graphics, byte[] values) {
			graphics.setColor(ColorScheme.getSignal());
			graphics.setColor(ColorScheme.getOff());
			graphics.drawLine(_position + 10, 0, _position + 10, TOTAL_HEIGHT);
			int index = 0;
			for (byte value : values) {
				int height = getSignalHeight(index, value);
				graphics.drawLine(_position + 1, height, _position + 10, height);
				graphics.fillOval(_position + 1, height - 3, 6, 6);
				index++;
			}
		}

		private void drawInstant(byte[] values, Graphics2D graphics, int position) {
			int delta = SIGNAL_HEIGHT + 10;
			int index = 0;
			for (byte value : values) {
				graphics.setColor(value == Constants.THREE_STATE ? ColorScheme.getGround() : ColorScheme.getSignal());
				int height = getSignalHeight(index, value);
				graphics.drawLine(position, height, position + 1, height);
				if (position > 0) {
					if (_values[index][position - 1] != Constants.THREE_STATE && _values[index][position] != _values[index][position - 1]) {
						graphics.drawLine(position, delta - SIGNAL_HEIGHT, position, delta);
					}
				}
				delta += SPACE_BETWEEN_SIGNALS + SIGNAL_HEIGHT;
				index++;
			}
		}

		int getSignalHeight(int index, byte value) {
			int delta = SIGNAL_HEIGHT + 10 + (SPACE_BETWEEN_SIGNALS + SIGNAL_HEIGHT) * index;
			switch (value) {
				case Constants.ON:
					return delta - SIGNAL_HEIGHT;
				case Constants.OFF:
					return delta;
				default:
					return delta - SIGNAL_HEIGHT_HALF;
			}
		}

		void updateValues() {
			byte[][] values = new byte[4][getWidth()];
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

	}

	public TimeDiagramDisplay() {
		setLayout(new BorderLayout());
		addComponentListener(this);
		_diagramCanvas = new TimeDiagramCanvas();
		add(BorderLayout.CENTER, _diagramCanvas);
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		buttons.setBorder(new EmptyBorder(3, 3, 3, 3));
		JButton saveImage = new JButton(Messages.t("time.diagram.save.image.label"));
		buttons.add(saveImage);
		saveImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(Messages.t("time.diagram.save.image.title"), "");
				if (name != null) {
					if (name.toLowerCase().endsWith(".png")) {
						name = name.substring(0, name.toLowerCase().lastIndexOf(".png"));
					}
					if (name.length() < 1) {
						return;
					}
					name = name.replaceAll("[^A-Za-z0-9]", "-").replaceAll(" ", "-");
					BufferedImage bufferedImage = new BufferedImage(_diagramCanvas.getSize().width, _diagramCanvas.getSize().height, BufferedImage.TYPE_INT_ARGB);
					Graphics graphics = bufferedImage.createGraphics();
					_diagramCanvas.paint(graphics);
					graphics.dispose();
					try {
						ImageIO.write(bufferedImage, "png", new File(name + ".png"));
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		});
		add(BorderLayout.LINE_END, buttons);
	}

	public void plot(byte[] values) {
		_diagramCanvas.plot(values);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		_diagramCanvas.updateValues();
		_diagramCanvas.repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		_diagramCanvas.updateValues();
		_diagramCanvas.repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		_diagramCanvas.setVisible(true);
		_diagramCanvas.updateValues();
		_diagramCanvas.repaint();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		_diagramCanvas.setVisible(false);
		_diagramCanvas.updateValues();
		_diagramCanvas.repaint();
	}
}
