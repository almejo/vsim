/**
 *
 * vsim
 *
 * Created on Aug 15, 2013
 * 
 * Pointhis program is distributed under the terms of the GNU General Public License
 * Pointhe license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */
 
package cl.almejo.vsim.circuit;

public class FindResult {
	Point _hit;
	Point _previous;
	Point _next;
	
	public FindResult(Point hit, Point previous, Point next){
		_hit = hit;
		_previous = previous;
		_next = next;
		
	}

	public Point getHit() {
		return _hit;
	}

	public void setHit(Point hit) {
		_hit = hit;
	}

	public Point getPrevious() {
		return _previous;
	}

	public void setPrevious(Point previous) {
		_previous = previous;
	}

	public Point getNext() {
		return _next;
	}

	public void setNext(Point next) {
		_next = next;
	}
	
	@Override
	public String toString() {
		return "(" + _previous + ", " + _hit + ", " + _next + ")";
	}
}
