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

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.MarchingAnts;
import cl.almejo.vsim.circuit.Point;
import cl.almejo.vsim.gui.Draggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class IconGate extends Rectangle implements Draggable {
	private static final Logger LOGGER = LoggerFactory.getLogger(IconGate.class);
	private static final long serialVersionUID = 1L;
	private Gate _gate;
	private int _id;

	private AffineTransform _translateTransformation = new AffineTransform();
	private AffineTransform _rotateTransformation = new AffineTransform();
	private AffineTransform _transform = new AffineTransform();
	private AffineTransform _origianlTransformation;
	private boolean _selected;

	public IconGate(int id) {
		super();
		_id = id;
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
		if (!isSelected()) {
			drawFrame(graphics, 2, 8);
		}
		_gate.getGateDescriptor().paint(graphics, this);
		popMatrix(graphics);
	}

	public void drawDecorations(Graphics2D graphics) {
		pushMatrix(graphics);
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
		AffineTransform newTransform = new AffineTransform(graphics.getTransform());
		newTransform.concatenate(_transform);
		graphics.setTransform(newTransform);
	}

	@Override
	public Dimension getSize() {
		return _gate.getGateDescriptor().getSize();
	}

	public void moveTo(int xf, int yf) {
		setLocation(xf, yf);
		setTranslation(xf, yf);
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
		_transform.setToIdentity();
		_transform.concatenate(_translateTransformation);
		_transform.concatenate(_rotateTransformation);
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
		_transform.transform(point2d, point2d);
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

}
