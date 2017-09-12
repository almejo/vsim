package cl.almejo.vsim.gui.components;

import cl.almejo.vsim.circuit.CircuitCanvas;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class ZoomChanger extends JPanel {

	private final CircuitCanvas _canvas;

	public ZoomChanger(CircuitCanvas canvas) {
		_canvas = canvas;
		JSlider zoomSlider = new JSlider(1, 8, 4);
		FlatButton decreaseZoom = new FlatButton(" - ");
		decreaseZoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (zoomSlider.getValue() > 1) {
					zoomSlider.setValue(zoomSlider.getValue() - 1);
				}
			}
		});
		add(decreaseZoom);

		zoomSlider.setSnapToTicks(true);
		zoomSlider.setMinorTickSpacing(1);
		add(zoomSlider);

		FlatButton increaseZoom = new FlatButton(" + ");
		increaseZoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (zoomSlider.getValue() < 8) {
					zoomSlider.setValue(zoomSlider.getValue() + 1);
				}
			}
		});
		add(increaseZoom);
		add(getZoomDisplay(zoomSlider));
	}

	private JLabel getZoomDisplay(JSlider zoomSlider) {
		JLabel label = new JLabel("100%");
		zoomSlider.addChangeListener(e -> {
			JSlider slider = (JSlider) e.getSource();
			double zoom = 0.25 * slider.getValue();
			_canvas.setZoom(zoom);
			label.setText((int) (zoom * 100) + "%");
		});
		return label;
	}
}