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

import cl.almejo.vsim.circuit.*;
import cl.almejo.vsim.circuit.Point;
import cl.almejo.vsim.gui.Draggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.List;
import java.util.Map;

public class IconGate extends Rectangle implements Draggable, Configurable {
	private static final Logger LOGGER = LoggerFactory.getLogger(IconGate.class);
	private static final long serialVersionUID = 1L;
	private Gate _gate;
	private int _id;
	private int _rotation;

	private AffineTransform _translateTransformation = new AffineTransform();
	private AffineTransform _rotateTransformation = new AffineTransform();
	private AffineTransform _transformation = new AffineTransform();
	private AffineTransform _origianlTransformation;
	private boolean _selected;
	private Rectangle _extent;

	public IconGate(int id) {
		super();
		_id = id;
		computeExtent();
	}

	public IconGate(int id, Gate gate) {
		this(id);
		_gate = gate;
	}

	public Gate getGate() {
		return _gate;
	}

	public void setGate(Gate gate) {
		_gate = gate;
	}

	public void drawIcon(Graphics2D graphics) {
		pushMatrix(graphics);
		applyTransform(graphics);
		if (!isSelected()) {
			drawFrame(graphics, 2, 8);
		}
		_gate.getGateDescriptor().paint(graphics, this);
		popMatrix(graphics);
	}

	public void drawDecorations(Graphics2D graphics) {
		pushMatrix(graphics);
		applyTransform(graphics);
		if (isSelected()) {
			drawSelectedDecoration(graphics, 2);
		}
		popMatrix(graphics);
	}

	private void drawSelectedDecoration(Graphics2D graphics, int separation) {
		Dimension dimension = _gate.getGateDescriptor().getSize();
		MarchingAnts.drawRect(graphics, -separation, -separation, (int) dimension.getWidth() + separation * 2, (int) dimension.getHeight() + separation * 2);
	}

	private void drawFrame(Graphics2D graphics, int separation, int border) {
		Dimension dimension = _gate.getGateDescriptor().getSize();
		graphics.setColor(Color.GRAY);
		graphics.drawLine(-(separation + border), -separation, dimension.width + (border + separation), -separation);
		graphics.drawLine(-separation, -(separation + border), -separation, dimension.height + separation + border);
		graphics.drawLine(dimension.width + separation, -(separation + border), dimension.width + separation, dimension.height + separation + border);
		graphics.drawLine(-(separation + border), dimension.height + separation, dimension.width + separation + border, dimension.height + separation);
	}

	private void popMatrix(Graphics2D graphics) {
		graphics.setTransform(_origianlTransformation);
	}

	private void pushMatrix(Graphics2D graphics) {
		_origianlTransformation = graphics.getTransform();
	}

	private void applyTransform(Graphics2D graphics) {
		AffineTransform newTransform = new AffineTransform(graphics.getTransform());
		newTransform.concatenate(_transformation);
		graphics.setTransform(newTransform);
	}

	@Override
	public Dimension getSize() {
		return _gate.getGateDescriptor().getSize();
	}

	public void moveTo(int xf, int yf) {
		setLocation(xf, yf);
		setTranslation(xf, yf);
		computeExtent();
	}

	public void setRotation(int rotation) {
		_gate.getCircuit().desactivate(this);
		updateRotation(rotation);
		_gate.getCircuit().activate(this);
		computeExtent();
	}

	private void updateRotation(int rotation) {
		_rotation = rotation % 4;
		_rotateTransformation.setToIdentity();
		_rotateTransformation.rotate(Math.toRadians(_rotation * 90));
		recalculateTransform();
	}

	public int getRotation() {
		return _rotation;
	}

	private void setTranslation(int x, int y) {
		LOGGER.debug("original transformation: " + _translateTransformation);
		_translateTransformation.setToIdentity();
		_translateTransformation.translate(x, y);
		LOGGER.debug("Translate to: " + x + " - " + y);
		recalculateTransform();
		LOGGER.debug("resulting transformation: " + _translateTransformation.toString());
	}

	private void recalculateTransform() {
		_transformation.setToIdentity();
		_transformation.concatenate(_translateTransformation);
		_transformation.concatenate(_rotateTransformation);
	}

	public int getPinsCount() {
		return _gate.getPinCount();
	}

	public Point getPinPos(byte pinId) {
		return _gate.getGateDescriptor().getPinPosition(pinId);
	}

	public Pin getPin(int i) {
		return _gate.getPin(i);
	}

	public Point getTransformed(Point point) {
		java.awt.geom.Point2D.Double point2d = new java.awt.geom.Point2D.Double(point.getX(), point.getY());
		_transformation.transform(point2d, point2d);
		return new Point((int) point2d.getX(), (int) point2d.getY());
	}

	public Point getTransformedPinPos(byte pinId) {
		return getTransformed(getPinPos(pinId));
	}

	public IconGate getInstance(Circuit circuit) {
		IconGate iconGate = new IconGate(circuit.getNextGateId());
		iconGate.setGate(_gate.getGateDescriptor().make(circuit, _gate.getParamameters()));
		return iconGate;
	}

	public int getId() {
		return _id;
	}

	@Override
	public void select() {
		_selected = true;
	}

	@Override
	public void deselect() {
		_selected = false;
	}

	@Override
	public boolean isSelected() {
		return _selected;
	}

	@Override
	public void drawPreview(Graphics2D graphics, double x, double y) {
		pushMatrix(graphics);

		AffineTransform transformation = new AffineTransform(graphics.getTransform());
		transformation.translate(-x, -y);

		graphics.setTransform(transformation);
		applyTransform(graphics);
		_gate.getGateDescriptor().paint(graphics, this);
		popMatrix(graphics);
	}

	@Override
	public void beforeDrag() {

	}

	@Override
	public void drag(int x, int y) {
		getGate().getCircuit().remove(this);
		getGate().getCircuit().add(this, x, y);
	}

	@Override
	public void afterDrag() {

	}

	@Override
	public int getOriginalX() {
		return (int) getX();
	}

	@Override
	public int getOriginalY() {
		return (int) getY();
	}

	@Override
	public boolean contains(int x, int y) {
		java.awt.Point.Double point = new java.awt.Point.Double(x - getX(), y - getY());
		java.awt.Point.Double transformed = new java.awt.Point.Double();
		try {
			_rotateTransformation.inverseTransform(point, transformed);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
			return false;
		}
		java.awt.Point.Double moved = new java.awt.Point.Double(transformed.getX() + getX(), transformed.getY() + getY());
		return super.contains(moved);
	}

	@Override
	public void rotateClockwise() {
		setRotation(getRotation() + 1);
	}

	@Override
	public void rotateCounterClockwise() {
		setRotation(getRotation() - 1);
	}

	@Override
	public List<ConfigVariable> getConfigVariables() {
		return _gate.getParamameters().getValues();
	}

	@Override
	public boolean isConfigurable() {
		return _gate.getParamameters().isConfigurable();
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		_gate.getParamameters().setValues(parameters);
		_gate.parametersUpdated();
	}

	@Override
	public boolean isCompilable() {
		return _gate instanceof  Compilable;
	}

	public void compile(Circuit circuit) {
		((Compilable) _gate).compile(circuit, getTransformed(_gate.getGateDescriptor().getPinPositions()));
	}

	private Point[] getTransformed(Point[] positions) {
		Point [] points = new Point[positions.length];
		for (int i = 0; i < positions.length; i++) {
			points[i] = getTransformed(positions[i]);
		}
		return points;
	}

	public Rectangle getExtent() {
		return _extent;
	}

	private void computeExtent() {
		java.awt.Point.Double point0 = new java.awt.Point.Double(0, 0);
		java.awt.Point.Double point1 = new java.awt.Point.Double(getWidth(), 0);
		java.awt.Point.Double point2 = new java.awt.Point.Double(getWidth(), getHeight());
		java.awt.Point.Double point3 = new java.awt.Point.Double(0, getHeight());

		java.awt.Point.Double transformed0 = new java.awt.Point.Double();
		java.awt.Point.Double transformed1 = new java.awt.Point.Double();
		java.awt.Point.Double transformed2 = new java.awt.Point.Double();
		java.awt.Point.Double transformed3 = new java.awt.Point.Double();

		_rotateTransformation.transform(point0, transformed0);
		_rotateTransformation.transform(point1, transformed1);
		_rotateTransformation.transform(point2, transformed2);
		_rotateTransformation.transform(point3, transformed3);

		java.awt.Point.Double[] points = new java.awt.Point.Double[]{
				transformed0
				, transformed1
				, transformed2
				, transformed3
		};
		int minx = (int) transformed0.x;
		int miny = (int) transformed0.y;
		int maxx = (int) transformed0.x;
		int maxy = (int) transformed0.y;
		for (java.awt.Point.Double point : points) {
			if (point.getX() < minx) {
				minx = (int) point.getX();
			}
			if (point.getY() < miny) {
				miny = (int) point.getY();
			}
			if (point.getX() > maxx) {
				maxx = (int) point.getX();
			}
			if (point.getY() > maxy) {
				maxy = (int) point.getY();
			}
		}
		Rectangle rectangle = new Rectangle(minx, miny, maxx - minx, maxy - miny);
		rectangle.translate((int) getX(), (int) getY());
		_extent = rectangle;
	}
}
