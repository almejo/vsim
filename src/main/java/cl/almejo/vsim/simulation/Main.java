/**
 *
 * vsim
 *
 * Created on Aug 12, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.simulation;

import cl.almejo.vsim.gates.Clock;
import cl.almejo.vsim.gates.ClockParams;

public class Main {
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();

		new Clock(scheduler, new ClockParams(1000, 3000));
		scheduler.run(10000);

	}
}
