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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class CanvasScrollPane extends JPanel implements AdjustmentListener, ViewportListener {
	private final JScrollBar _horizontalScrollbar;
	private final JScrollBar _verticalScrollbar;
	private final CircuitCanvas _canvas;

	private static final Logger LOGGER = LoggerFactory.getLogger(CanvasScrollPane.class);

	public CanvasScrollPane(CircuitCanvas canvas) {
		setLayout(new GridBagLayout());
		_canvas = addCanvas(canvas);
		_canvas.addViewportListener(this);
		_verticalScrollbar = addVerticalScrollbar();
		_horizontalScrollbar = addHorizontalScrollbar();
		updateScrollbars();
	}

	private void updateScrollbars() {
		Rectangle world = _canvas.getWorld();
		LOGGER.debug("World: " + world);
		Rectangle viewport = _canvas.getViewport();
		LOGGER.debug("Viewport: " + viewport);

		_horizontalScrollbar.setMinimum(world.x);
		_horizontalScrollbar.setMaximum(world.width);
		_horizontalScrollbar.setValue(viewport.x);
		_horizontalScrollbar.setVisibleAmount(viewport.width);

		_verticalScrollbar.setMinimum(world.y);
		_verticalScrollbar.setMaximum(world.height);
		_verticalScrollbar.setValue(viewport.y);
		_verticalScrollbar.setVisibleAmount(viewport.height);
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
	public void adjustmentValueChanged(AdjustmentEvent event) {
	}

	@Override
	public void updated(CircuitCanvas circuitCanvas) {
		updateScrollbars();
	}
}
