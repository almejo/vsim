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

	public static Stroke getStroke() {
		return new BasicStroke(2f
				, BasicStroke.CAP_BUTT
				, BasicStroke.JOIN_MITER
				, 1.5f
				, DASH
				, DASH_PHASE);
	}
}
