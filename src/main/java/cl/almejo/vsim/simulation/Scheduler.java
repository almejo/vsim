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


public class Scheduler {

	class StopEvent extends SimulationEvent {
		public StopEvent(Scheduler scheduler) {
			super(scheduler);
		}

		@Override
		public void happen() {
			_scheduler.stop();
		}

	}

	private Heap _heap = new Heap();

	private long _time = 0;

	@SuppressWarnings("InfiniteLoopStatement")
	public void run(long simulationTime) {
		schedule(new StopEvent(this), simulationTime);
		try {
			while (true) {
				SimulationEvent event = (SimulationEvent) _heap.remove();
				_time = event.getTime();
				event.happen();
			}
		} catch (EmptyHeapException e) {
			e.printStackTrace();
		} catch (StopSimulationException e) {
			//System.out.println("Simulacion detenida");
		}
	}

	public void stop() throws StopSimulationException {
		throw new StopSimulationException();
	}

	public void schedule(HeapElement element, long delay) {
		element.setValue(_time + delay);
		_heap.insert(element);
	}

	public boolean isProgrammed(SimulationEvent simulationEvent) {
		return _heap.contains(simulationEvent);
	}

}
