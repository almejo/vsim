package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gui.ColorScheme;
import cl.almejo.vsim.gui.ViewportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class CircuitCanvas extends JPanel implements ComponentListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CircuitCanvas.class);

	private static final long serialVersionUID = 1L;

	private Circuit circuit;
	private final AffineTransform translationTransformation = new AffineTransform();
	private final AffineTransform computedTransformation = new AffineTransform();
	private final AffineTransform zoomTransformation = new AffineTransform();
	private double zoom = 1.0;
	private final Rectangle viewport = new Rectangle();
	private final List<ViewportListener> viewportListeners = new LinkedList<>();

	public CircuitCanvas(Circuit circuit) {
		this.circuit = circuit;
		this.circuit.add(this);
		addComponentListener(this);
		setPreferredSize(new Dimension(800, 1800));
		resizeViewport();
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		clean(graphics2D);

		AffineTransform transform = new AffineTransform(computedTransformation);
		transform.concatenate(graphics2D.getTransform());

		graphics2D.setTransform(transform);
		circuit.paint(graphics2D, viewport);
		graphics2D.setTransform(Constants.TRANSFORM_IDENTITY);
	}

	private void clean(Graphics2D graphics2D) {
		graphics2D.setColor(ColorScheme.getBackground());
		graphics2D.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());
	}

	public void resizeViewport() {
		Dimension size = getSize();
		viewport.setRect(viewport.getX(), viewport.getY(), size.getWidth() / zoom, size.getHeight() / zoom);
		sendViewportUpdatedEvent();
	}

	public void setCircuit(Circuit circuit) {
		this.circuit = circuit;
		this.circuit.add(this);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		resizeViewport();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent event) {
	}

	@Override
	public void componentHidden(ComponentEvent event) {
	}

	public void centerViewportTo(int x, int y) {
		moveViewport(Circuit.gridTrunc((int) viewport.getCenterX() - x), Circuit.gridTrunc((int) viewport.getCenterY() - y));
	}

	private void translateViewportTo(int x, int y) {
		int dx = x - (int) viewport.getX();
		int dy = y - (int) viewport.getY();
		viewport.translate(dx, dy);
		translationTransformation.translate(-dx, -dy);
		updateTransformation();
		sendViewportUpdatedEvent();
	}

	public void moveViewport(int deltaX, int deltaY) {
		viewport.translate(-deltaX, -deltaY);
		translationTransformation.translate(deltaX, deltaY);
		updateTransformation();
		sendViewportUpdatedEvent();
	}

	private void updateTransformation() {
		computedTransformation.setToIdentity();
		computedTransformation.concatenate(zoomTransformation);
		computedTransformation.concatenate(translationTransformation);
	}

	public int toCircuitCoordinatesX(int x) {
		return (int) ((double) x / zoom + viewport.getX());
	}

	public int toCircuitCoordinatesY(int y) {
		return (int) ((double) y / zoom + viewport.getY());
	}

	public void setZoom(double zoom) {
		double centerX = viewport.getCenterX();
		double centerY = viewport.getCenterY();
		viewport.setFrame(viewport.getX(), viewport.getY(), viewport.getWidth() * this.zoom, viewport.getHeight() * this.zoom);
		viewport.setFrame(viewport.getX(), viewport.getY(), viewport.getWidth() / zoom, viewport.getHeight() / zoom);
		zoomTransformation.setToIdentity();
		zoomTransformation.scale(zoom, zoom);
		int cornerX = (int) (centerX - viewport.getWidth() / 2);
		int cornerY = (int) (centerY - viewport.getHeight() / 2);
		translateViewportTo(cornerX, cornerY);
		this.zoom = zoom;
	}

	public double getZoom() {
		return zoom;
	}

	public Rectangle getWorld() {
		Rectangle rectangle = new Rectangle(viewport);
		if (circuit.getExtent() != null) {
			rectangle.add(circuit.getExtent());
		}
		return rectangle;
	}

	public Rectangle getViewport() {
		return new Rectangle(viewport);
	}

	public void addViewportListener(ViewportListener listener) {
		if (listener != null) {
			viewportListeners.add(listener);
		}
	}

	private void sendViewportUpdatedEvent() {
		for (ViewportListener listener : viewportListeners) {
			listener.updated(this);
		}
	}

	public void center() {
		if (circuit.getExtent() == null) {
			return;
		}
		Rectangle extent = circuit.getExtent();
		centerViewportTo((int) extent.getCenterX(), (int) extent.getCenterY());
		LOGGER.debug("Viewport centered");
	}
}
