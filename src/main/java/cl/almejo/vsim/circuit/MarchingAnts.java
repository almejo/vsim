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

package cl.almejo.vsim.circuit;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

public class MarchingAnts {
	private static final float[] DASH = {5.0f, 5.0f};
	private static float DASH_PHASE = 0f;

	private static TimerTask TASK = new TimerTask() {
		@Override
		public void run() {
			DASH_PHASE += 0.9f;
		}
	};

	static {
		new Timer().schedule(TASK, 100, 100);
	}

	private static Stroke getStroke() {
		return new BasicStroke(2f
				, BasicStroke.CAP_BUTT
				, BasicStroke.JOIN_MITER
				, 1.5f
				, DASH
				, DASH_PHASE);
	}

	public static void drawLines(Graphics2D graphics, List<Point[]> points) {
		if (points == null) {
			return;
		}

		Stroke oldStroke = graphics.getStroke();
		graphics.setColor(Color.YELLOW);
		draw(graphics, points);
		graphics.setStroke(getStroke());
		graphics.setColor(Color.BLACK);
		draw(graphics, points);
		graphics.setStroke(oldStroke);
	}

	private static void draw(Graphics2D graphics, List<Point[]> points) {
		for (Point[] point : points) {
			graphics.drawLine(point[0].getX(), point[0].getY(), point[1].getX(), point[1].getY());
		}
	}

	public static void drawRect(Graphics2D graphics, int x, int y, int width, int height) {
		Stroke oldStroke = graphics.getStroke();
		graphics.setColor(Color.YELLOW);
		graphics.drawRect(x, y, width, height);
		graphics.setColor(Color.BLACK);
		graphics.setStroke(getStroke());
		graphics.drawRect(x, y, width, height);
		graphics.setStroke(oldStroke);
	}
}
