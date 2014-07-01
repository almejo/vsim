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

package cl.almejo.vsim.gui;

import cl.almejo.vsim.circuit.CircuitCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class CanvasScrollPane extends JPanel implements AdjustmentListener {
	private final JScrollBar _horizontalScrollbar;
	private final JScrollBar _verticalScrollbar;
	private final CircuitCanvas _canvas;

	public CanvasScrollPane(CircuitCanvas canvas) {
		setLayout(new GridBagLayout());
		_canvas = addCanvas(canvas);
		_verticalScrollbar = addVerticalScrollbar();
		_horizontalScrollbar = addHorizontalScrollbar();
	}

	private CircuitCanvas addCanvas(CircuitCanvas canvas) {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1.0;
		constraint.weighty = 1.0;
		add(canvas, constraint);
		return canvas;
	}

	private JScrollBar addVerticalScrollbar() {
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.addAdjustmentListener(this);
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 0;
		constraint.anchor = GridBagConstraints.NORTHEAST;
		constraint.fill = GridBagConstraints.VERTICAL;
		add(scrollBar, constraint);
		return scrollBar;
	}
	private JScrollBar addHorizontalScrollbar() {
		JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollBar.addAdjustmentListener(this);
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.anchor = GridBagConstraints.SOUTHWEST;
		constraint.fill = GridBagConstraints.HORIZONTAL;
		add(scrollBar, constraint);
		return scrollBar;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		System.out.println(e);
	}
}
