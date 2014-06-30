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

package cl.almejo.vsim.gui.components;

import cl.almejo.vsim.circuit.CircuitCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ZoomChanger extends JPanel {

	private CircuitCanvas _canvas;

	public ZoomChanger(CircuitCanvas canvas) {
		_canvas = canvas;
		final JSlider zoomSlider = new JSlider(1, 8, 4);
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
		final JLabel label = new JLabel("100%");
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				double zoom = 0.25 * slider.getValue();
				_canvas.setZoom(zoom);
				label.setText(((int) (zoom * 100)) + "%");
			}
		});
		return label;
	}
}