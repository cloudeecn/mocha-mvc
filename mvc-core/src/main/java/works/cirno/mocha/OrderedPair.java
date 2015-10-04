package works.cirno.mocha;

public class OrderedPair<A, B> {
	private A first;
	private B second;

	private int hash;

	public OrderedPair(A first, B second) {
		super();
		this.first = first;
		this.second = second;
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		hash = result;
	}

	A getFirst() {
		return first;
	}

	B getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedPair<?, ?> other = (OrderedPair<?, ?>) obj;
		if(hash != other.hash){
			return false;
		}
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

}
