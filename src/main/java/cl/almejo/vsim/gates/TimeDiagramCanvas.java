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
import java.awt.*;

public class TimeDiagramCanvas extends JPanel implements DisplayPanel {

	private byte[][] _values = new byte[4][1000];

	private int _position = 0;
	private static final int SIGNAL_HEIGHT = 20;
	private static final int SPACE_BETWEEN_SIGNALS = 10;
	private static final int SIGNAL_HEIGHT_HALF = SIGNAL_HEIGHT / 2;

	public TimeDiagramCanvas() {
		setPreferredSize(new Dimension(1000, 200));
		setBackground(ColorScheme.getBackground());
	}


	@Override
	public void paint(Graphics graphics) {
//		System.out.println("PAINT!!!");
		super.paint(graphics);

//		for (int i = 0; i < _values[0].length; i++) {
//			drawInstant(new byte[]{_values[0][i], _values[1][i], _values[2][i], _values[3][i]}, (Graphics2D) graphics, i);
//		}
	}

	public void plot(byte[] values) {
		if (_position >= _values[0].length) {
			_position = 0;
		}
		for (int i = 0; i < values.length; i++) {
			_values[i][_position] = values[i];
		}
		if (!isVisible()) {
			return;
		}
		drawInstant(values, (Graphics2D) getGraphics(), _position);
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
		graphics.setColor(ColorScheme.getBackground());
		graphics.fillRect(_position + 2, 0, 20 , getHeight());
	}
}
