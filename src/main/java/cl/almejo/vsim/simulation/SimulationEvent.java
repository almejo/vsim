/**
 *
 * vsim
 *
 * Created on Aug 1, 2013
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.simulation;

public abstract class SimulationEvent extends HeapElement {

	protected Scheduler _scheduler;

	public SimulationEvent(Scheduler scheduler) {
		_scheduler = scheduler;
	}

	public void schedule(long delay) {
		_scheduler.schedule(this, delay);
	}

	abstract public void happen();

	public long getTime() {
		return getValue();
	}

	public boolean isProgrammed() {
		return _scheduler.isProgrammed(this);
	}
}
