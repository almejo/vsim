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

public class TimeDiagramCanvas extends JPanel {

	private byte[][] _values = new byte[4][1000];

	private int _position = 0;
	private static final int SIGNAL_HEIGHT = 20;
	private static final int SPACE_BETWEEN_SIGNALS = 10;
	private static int SIGNAL_HEIGHT_HALF = SIGNAL_HEIGHT / 2;
	;

	public TimeDiagramCanvas() {
		setPreferredSize(new Dimension(1000, 200));
	}

	public void plot(byte[] values) {
		if (_position >= _values[0].length) {
			_position = 0;
		}
		for (int i = 0; i < values.length; i++) {
			_values[i][_position] = values[i];
		}
		int delta = SIGNAL_HEIGHT + 10;
		Graphics2D graphics = (Graphics2D) getGraphics();
		int index = 0;
		for (byte value : values) {
			graphics.setColor(ColorScheme.getWireOn());;
			switch (value) {
				case Constants.ON:
					graphics.drawLine(_position, delta - SIGNAL_HEIGHT, _position + 1, delta - SIGNAL_HEIGHT);
					break;
				case Constants.OFF:
					graphics.drawLine(_position, delta, _position + 1, delta);
					break;
				default:
					graphics.setColor(ColorScheme.getGround());
					graphics.drawLine(_position, delta - SIGNAL_HEIGHT_HALF, _position + 1, delta - SIGNAL_HEIGHT_HALF);
					break;
			}

			if (_position > 0) {
				if (_values[index][_position - 1] != Constants.THREE_STATE && _values[index][_position] != _values[index][_position - 1]) {
					graphics.drawLine(_position, delta - SIGNAL_HEIGHT, _position, delta);
				}
			}
			delta += SPACE_BETWEEN_SIGNALS + SIGNAL_HEIGHT;
			index++;
		}

		_position++;
	}

	public void clean() {
		Dimension dimension = getSize();
		Graphics2D graphics = (Graphics2D) getGraphics();
		graphics.setColor(ColorScheme.getBackground());
		graphics.fillRect(0, 0, dimension.width, dimension.height);
	}
}
