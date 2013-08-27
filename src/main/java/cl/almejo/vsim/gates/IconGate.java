package cl.almejo.vsim.gates;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

public class IconGate extends Rectangle {

	private static final long serialVersionUID = 1L;
	Gate _gate;

	private AffineTransform _translateTransformation = new AffineTransform();
	private AffineTransform _rotateTransformation = new AffineTransform();
	private AffineTransform _transform = new AffineTransform();
	private AffineTransform _origianlTransformation;

	public IconGate() {
		super();
	}
	
	public IconGate(Gate gate) {
		_gate = gate;
	}

	public Gate getGate() {
		return _gate;
	}

	public void setGate(Gate gate) {
		_gate = gate;
	}

	public void paint(Graphics2D graphics) {
		pushMatrix(graphics);
		_gate.getGateDescriptor().paint(graphics, this);
		popMatrix(graphics);
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
	
	public Dimension getSize() {
		return _gate.getGateDescriptor().getSize();
	}

	public void moveTo(int xf, int yf) {
		setLocation(xf, yf);
		setTranslation(xf, yf);
		
	}

	private void setTranslation(int x, int y) {
		System.out.println(_translateTransformation);
		_translateTransformation.setToIdentity();
		_translateTransformation.translate(x, y);
		System.out.println(x + " - " + y);
		recalculateTransform();
		System.out.println(_translateTransformation);
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
		IconGate iconGate = new IconGate();

		iconGate.setGate(_gate.getGateDescriptor().make(circuit, _gate.getParams()));
		return iconGate;
	}
}
