package cl.almejo.vsim.gui;

import java.awt.image.BufferedImage;

public interface Selection {

	public void add(Draggable Draggable) ;
	public void remove(Draggable Draggable);
	public void clear();
	public BufferedImage getImage();
}
