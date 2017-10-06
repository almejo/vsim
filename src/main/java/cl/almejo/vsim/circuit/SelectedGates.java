package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gui.Selectable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class SelectedGates {
	List<Selectable> selectables = new LinkedList<>();
	Rectangle extent;

	public void add(Selectable selectable) {
		selectable.select();
		selectables.add(selectable);
		updateExtent();
	}

	public void remove(Selectable selectable) {
		selectable.deselect();
		selectables.remove(selectable);
	}

	public void clear() {
		selectables.forEach(Selectable::deselect);
		selectables.clear();
	}

	public BufferedImage getImage() {
		if (selectables.isEmpty()) {
			return null;
		}
		updateExtent();
		BufferedImage bufferedImage = new BufferedImage(extent.width, extent.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bufferedImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		selectables.forEach(selection -> selection.drawPreview(graphics, extent.getX(), extent.getY()));
		return bufferedImage;
	}

	public List<Selectable> getSelectables() {
		List<Selectable> draggables = new LinkedList<>();
		draggables.addAll(this.selectables);
		return draggables;
	}

	void updateExtent() {
		if (selectables.isEmpty()) {
			extent = new Rectangle();
			return;
		}
		extent = new Rectangle(selectables.get(0).getExtent());
		selectables.forEach(draggable -> extent.add(draggable.getExtent()));
	}

	public int getX() {
		return (int) extent.getX();
	}

	public int getY() {
		return (int) extent.getY();
	}

	boolean contains(int x, int y) {
		for (Selectable selectable : selectables) {
			if (selectable.contains(x, y)) {
				return true;
			}
		}
		return false;
	}
}
