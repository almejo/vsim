/**
 *
 * vsim
 *
 * Created on Aug 15, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */
 
package cl.almejo.vsim.circuit;

public class FindResult<T extends Point> {
	T _hit;
	T _previous;
	T _next;
	
	public FindResult(T hit, T previous, T next){
		_hit = hit;
		_previous = previous;
		_next = next;
		
	}

	public T getHit() {
		return _hit;
	}

	public void setHit(T hit) {
		_hit = hit;
	}

	public T getPrevious() {
		return _previous;
	}

	public void setPrevious(T previous) {
		_previous = previous;
	}

	public T getNext() {
		return _next;
	}

	public void setNext(T next) {
		_next = next;
	}
	
	@Override
	public String toString() {
		return "(" + _previous + ", " + _hit + ", " + _next + ")";
	}
}
