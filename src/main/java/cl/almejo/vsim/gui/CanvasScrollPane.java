package cl.almejo.vsim.gui;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.CircuitCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class CanvasScrollPane extends JPanel implements AdjustmentListener, ViewportListener {
	private final JScrollBar horizontalScrollbar;
	private final JScrollBar verticalScrollbar;
	private final CircuitCanvas canvas;

	CanvasScrollPane(CircuitCanvas canvas) {
		setLayout(new GridBagLayout());
		this.canvas = addCanvas(canvas);
		this.canvas.addViewportListener(this);
		verticalScrollbar = addVerticalScrollbar();
		horizontalScrollbar = addHorizontalScrollbar();
		updateScrollbars();
	}

	private void updateScrollbars() {
		SwingUtilities.invokeLater(() -> {
			Rectangle world = canvas.getWorld();
			Rectangle viewport = canvas.getViewport();

			horizontalScrollbar.setMinimum(world.x);
			horizontalScrollbar.setMaximum(world.x + world.width);
			horizontalScrollbar.setValue(viewport.x);
			horizontalScrollbar.setVisibleAmount(viewport.width);
			horizontalScrollbar.setBlockIncrement(getHorizontalBlockIncrement());

			verticalScrollbar.setMinimum(world.y);
			verticalScrollbar.setMaximum(world.y + world.height);
			verticalScrollbar.setValue(viewport.y);
			verticalScrollbar.setVisibleAmount(viewport.height);
			verticalScrollbar.setBlockIncrement(getVerticalBlockIncrement());
		});
	}

	private int getHorizontalBlockIncrement() {
		return Math.min(horizontalScrollbar.getVisibleAmount(), horizontalScrollbar.getMaximum() - (horizontalScrollbar.getMinimum() + horizontalScrollbar.getVisibleAmount()));
	}

	private int getVerticalBlockIncrement() {
		return Math.min(verticalScrollbar.getVisibleAmount(), verticalScrollbar.getMaximum() - verticalScrollbar.getMinimum());
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
		scrollBar.setUnitIncrement(Circuit.gridTrunc((int) (Circuit.GRID_SIZE / canvas.getZoom())));
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 0;
		constraint.anchor = GridBagConstraints.NORTHEAST;
		constraint.fill = GridBagConstraints.VERTICAL;
		add(scrollBar, constraint);
		return scrollBar;
	}

	private JScrollBar addHorizontalScrollbar() {
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar.setUnitIncrement(Circuit.gridTrunc((int) (Circuit.GRID_SIZE / canvas.getZoom())));
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
		JScrollBar scrollbar = (JScrollBar) event.getSource();
		if (scrollbar == horizontalScrollbar) {
			canvas.moveViewport(canvas.getViewport().x - scrollbar.getValue(), 0);
		} else {
			canvas.moveViewport(0, canvas.getViewport().y - scrollbar.getValue());
		}
	}

	void addCorner(JComponent component) {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 1;
		constraint.anchor = GridBagConstraints.SOUTHEAST;
		constraint.fill = GridBagConstraints.BOTH;
		add(component, constraint);
	}

	@Override
	public void updated(CircuitCanvas circuitCanvas) {
		updateScrollbars();
	}
}
