package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gui.Draggable;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Selection{

	public void add(Draggable selectable) ;
	public void remove(Draggable selectable);
	public void clear();
	public BufferedImage getImage();
	List<Draggable> getDraggables();
	int getX();
	int getY();
}
