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
	private Link link = this;

	private Link find(Link link) {
		Link currentLink = this.link;
		do {
			if (currentLink == link) {
				return link;
			}
			currentLink = link;
		} while (currentLink != this);
		return null;
	}

	void join(Link link) {
		if (find(link) != null) {
			link.link = this.link;
			this.link = link;
		}
	}

	Link getNext() {
		return link;
	}

	void delete() {
		Link previous = findPrevious();
		if (previous != null) {
			previous.link = link;
			link = this;
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
