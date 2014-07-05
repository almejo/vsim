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

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.CircuitCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class CanvasScrollPane extends JPanel implements AdjustmentListener, ViewportListener {
	private final JScrollBar _horizontalScrollbar;
	private final JScrollBar _verticalScrollbar;
	private final CircuitCanvas _canvas;

	public CanvasScrollPane(CircuitCanvas canvas) {
		setLayout(new GridBagLayout());
		_canvas = addCanvas(canvas);
		_canvas.addViewportListener(this);
		_verticalScrollbar = addVerticalScrollbar();
		_horizontalScrollbar = addHorizontalScrollbar();
		updateScrollbars();
	}

	private void updateScrollbars() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Rectangle world = _canvas.getWorld();
				Rectangle viewport = _canvas.getViewport();

				_horizontalScrollbar.setMinimum(world.x);
				_horizontalScrollbar.setMaximum(world.x + world.width);
				_horizontalScrollbar.setValue(viewport.x);
				_horizontalScrollbar.setVisibleAmount(viewport.width);
				_horizontalScrollbar.setBlockIncrement(getHorizontalBlockIncrement());

				_verticalScrollbar.setMinimum(world.y);
				_verticalScrollbar.setMaximum(world.y + world.height);
				_verticalScrollbar.setValue(viewport.y);
				_verticalScrollbar.setVisibleAmount(viewport.height);
				_verticalScrollbar.setBlockIncrement(getVerticalBlockIncrement());
			}
		});
	}

	private int getHorizontalBlockIncrement() {
		return Math.min(_horizontalScrollbar.getVisibleAmount(), _horizontalScrollbar.getMaximum() - (_horizontalScrollbar.getMinimum() + _horizontalScrollbar.getVisibleAmount()));
	}

	private int getVerticalBlockIncrement() {
		return Math.min(_verticalScrollbar.getVisibleAmount(), _verticalScrollbar.getMaximum() - _verticalScrollbar.getMinimum());
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
		scrollBar.setUnitIncrement(Circuit.gridTrunc((int) (Circuit.GRIDSIZE / _canvas.getZoom())));
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
		scrollBar.setUnitIncrement(Circuit.gridTrunc((int) (Circuit.GRIDSIZE / _canvas.getZoom())));
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
		JScrollBar scrollbar = ((JScrollBar) event.getSource());
		if (scrollbar == _horizontalScrollbar) {
			_canvas.moveViewport(_canvas.getViewport().x - scrollbar.getValue(), 0);
		} else {
			_canvas.moveViewport(0, _canvas.getViewport().y - scrollbar.getValue());
		}
	}

	public void addCorner(JComponent component) {
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
