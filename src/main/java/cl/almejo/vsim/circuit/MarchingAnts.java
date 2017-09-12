package cl.almejo.vsim.circuit;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class MarchingAnts {
	private static final float[] DASH = {5.0f, 5.0f};
	private static float dashPhase;

	private static final TimerTask TASK = new TimerTask() {
		@Override
		public void run() {
			dashPhase += 0.9f;
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
				, dashPhase);
	}

	static void drawLines(Graphics2D graphics, List<Point[]> points) {
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
		points.forEach(point -> graphics.drawLine(point[0].getX(), point[0].getY(), point[1].getX(), point[1].getY()));
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