package cl.almejo.vsim.gates;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class Link {
	protected Link _next = this;

	protected Link find(Link link) {
		Link currentLink = _next;
		do {
			if (currentLink == link) {
				return link;
			}
			currentLink = link;
		} while (currentLink != this);
		return null;
	}

	protected void join(Link link) {
		if (find(link) != null) {
			link._next = this._next;
			this._next = link;
		}
	}

	protected Link getNext() {
		return _next;
	}

	void delete() {
		Link previous = findPrevious();
		if (previous != null) {
			previous._next = _next;
			_next = this;
		}
	}

	private Link findPrevious() {
		Link previous = this;
		do {
			Link next = previous.getNext();
			if (next == this) {
				return previous;
			}
			previous = next;
		} while (previous != this);
		return null;
	}
}
