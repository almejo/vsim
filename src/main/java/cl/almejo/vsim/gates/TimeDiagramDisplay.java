package cl.almejo.vsim.gates;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.gui.ColorScheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class TimeDiagramDisplay extends JPanel implements DisplayPanel, ComponentListener {

	private final TimeDiagramCanvas timeDiagramCanvas;
	private static final int SIGNAL_HEIGHT = 20;
	private static final int SPACE_BETWEEN_SIGNALS = 10;
	private static final int SIGNAL_HEIGHT_HALF = SIGNAL_HEIGHT / 2;
	private static final int TOTAL_HEIGHT = (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4;

	private static class TimeDiagramCanvas extends JPanel {
		private byte[][] values = new byte[4][1000];
		private int position;

		TimeDiagramCanvas() {
			setBorder(new EmptyBorder(3, 3, 3, 3));
			setBackground(ColorScheme.getBackground());
			for (byte[] value : values) {
				Arrays.fill(value, Constants.THREE_STATE);
			}
			updateValues();
		}

		@Override
		public void paint(Graphics graphics) {
			super.paint(graphics);
			Graphics2D graphics2D = (Graphics2D) graphics;
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for (int i = 0; i < values[0].length; i++) {
				drawInstant(new byte[]{values[0][i], values[1][i], values[2][i], values[3][i]}, graphics2D, i);
			}
			graphics2D.setColor(ColorScheme.getGrid());
			graphics2D.drawLine(0, (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4 + SPACE_BETWEEN_SIGNALS, getWidth(), (SIGNAL_HEIGHT + SPACE_BETWEEN_SIGNALS) * 4 + SPACE_BETWEEN_SIGNALS);
			graphics2D.setColor(ColorScheme.getLabel());
		}

		void plot(byte[] values) {
			if (this.values[0].length < 1) {
				return;
			}
			if (position >= this.values[0].length) {
				position = 0;
			}
			for (int i = 0; i < values.length; i++) {
				this.values[i][position] = values[i];
			}
			if (!isVisible()) {
				return;
			}
			Graphics2D graphics = (Graphics2D) getGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(ColorScheme.getBackground());
			graphics.fillRect(position, 0, 20, TOTAL_HEIGHT + 6);

			drawInstant(values, graphics, position);

			drawPlotter(graphics, values);
			position++;
		}

		private void drawPlotter(Graphics2D graphics, byte[] values) {
			graphics.setColor(ColorScheme.getSignal());
			graphics.setColor(ColorScheme.getOff());
			graphics.drawLine(position + 10, 0, position + 10, TOTAL_HEIGHT);
			int index = 0;
			for (byte value : values) {
				int height = getSignalHeight(index, value);
				graphics.drawLine(position + 1, height, position + 10, height);
				graphics.fillOval(position + 1, height - 3, 6, 6);
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
					if (this.values[index][position - 1] != Constants.THREE_STATE && this.values[index][position] != this.values[index][position - 1]) {
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
			for (int i = 0; i < this.values.length; i++) {
				Arrays.fill(values[i], Constants.THREE_STATE);
				System.arraycopy(this.values[i], 0, values[i], 0, Math.min(getWidth(), this.values[i].length));
			}
			this.values = values;
			if (position >= getWidth()) {
				position = 0;
			}
			repaint();
		}

	}

	TimeDiagramDisplay() {
		setLayout(new BorderLayout());
		addComponentListener(this);
		timeDiagramCanvas = new TimeDiagramCanvas();
		add(BorderLayout.CENTER, timeDiagramCanvas);
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		buttons.setBorder(new EmptyBorder(3, 3, 3, 3));
		JButton saveImage = new JButton(Messages.t("time.diagram.save.image.label"));
		buttons.add(saveImage);
		saveImage.addActionListener(event -> {
			String name1 = JOptionPane.showInputDialog(Messages.t("time.diagram.save.image.title"), "");
			if (name1 != null) {
				if (name1.toLowerCase().endsWith(".png")) {
					name1 = name1.substring(0, name1.toLowerCase().lastIndexOf(".png"));
				}
				if (name1.length() < 1) {
					return;
				}
				name1 = name1.replaceAll("[^A-Za-z0-9]", "-").replaceAll(" ", "-");
				BufferedImage bufferedImage = new BufferedImage(timeDiagramCanvas.getSize().width, timeDiagramCanvas.getSize().height, BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.createGraphics();
				timeDiagramCanvas.paint(graphics);
				graphics.dispose();
				try {
					ImageIO.write(bufferedImage, "png", new File(name1 + ".png"));
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		add(BorderLayout.LINE_END, buttons);
	}

	void plot(byte[] values) {
		timeDiagramCanvas.plot(values);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		timeDiagramCanvas.updateValues();
		timeDiagramCanvas.repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		timeDiagramCanvas.updateValues();
		timeDiagramCanvas.repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		timeDiagramCanvas.setVisible(true);
		timeDiagramCanvas.updateValues();
		timeDiagramCanvas.repaint();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		timeDiagramCanvas.setVisible(false);
		timeDiagramCanvas.updateValues();
		timeDiagramCanvas.repaint();
	}
}
