package hr.fer.zemris.linalg;

public abstract class AbstractVector implements IVector {

	@Override
	public IVector copyPart(int n) {
		IVector newVector = newInstance(n);
		for (int idx = 0, dimension = getDimension(); idx < n; idx++) {
			if (idx < dimension) {
				newVector.set(idx, get(idx));
			} else {
				newVector.set(idx, 0);
			}
		}
		return newVector;
	}

	@Override
	public IVector add(IVector other) {
		iterateVector((idx) -> this.set(idx, this.get(idx) + other.get(idx)));
		
		return this;
	}

	@Override
	public IVector nAdd(IVector other) {
		return copy().add(other);
	}

	@Override
	public IVector sub(IVector other) {
		iterateVector((idx) -> this.set(idx, this.get(idx) - other.get(idx)));
		
		return this;
	}

	@Override
	public IVector nSub(IVector other) {
		return copy().sub(other);
	}

	@Override
	public IVector scalarMultiply(double byValue) {
		iterateVector((idx) -> this.set(idx, byValue*get(idx)));
		return this;
	}

	@Override
	public IVector nScalarMultiply(double byValue) {
		return copy().scalarMultiply(byValue);
	}

	@Override
	public double norm() {
		double sum = 0;
		for (int idx = 0, dimension = getDimension(); idx < dimension; idx++) {
			sum += Math.pow(get(idx), 2);
		}
		return Math.sqrt(sum);
	}

	@Override
	public IVector normalize() {
		double norm = norm();
		iterateVector((idx) -> set(idx, get(idx)/norm));
		return this;
	}

	@Override
	public IVector nNormalize() {
		return copy().normalize();
	}

	@Override
	public double cosine(IVector other) {
		return scalarProduct(other)/(this.norm()*other.norm());
	}

	@Override
	public double scalarProduct(IVector other) {
		int dimension = this.getDimension();
		double scalarProduct = 0;
		for (int idx = 0; idx < dimension; idx ++) {
			scalarProduct += get(idx)*other.get(idx);
		}
		
		return scalarProduct;
	}

	@Override
	public IVector nVectorProduct(IVector other) {
		IVector newVector = newInstance(3);
		newVector.set(0, get(1)*other.get(2) - get(2)*other.get(1));
		newVector.set(1, get(2)*other.get(0) - get(0)*other.get(2));
		newVector.set(2, get(0)*other.get(1) - get(1)*other.get(0));
		
		return newVector;
	}

	@Override
	public IVector nFromHomogeneous() {
		int dimension = getDimension();
		double lastElement = get(dimension-1);
		IVector newVector = newInstance(dimension-1);
		for (int idx = 0; idx < dimension -1; idx++) {
			newVector.set(idx, get(idx)/lastElement);
		}
		return newVector;
	}

	@Override
	public IMatrix toRowMatrix(boolean liveView) {
		if (liveView) {
			return new MatrixVectorView(this, true);
		} else {
			int dimension = getDimension();
			double[][] elements = new double[1][dimension];
			
			for (int idx = 0; idx < dimension; idx++) {
				elements[0][idx] = get(idx);
			}
			
			return new Matrix(1, dimension, elements, true);
		}
	}

	@Override
	public IMatrix toColumnMatrix(boolean liveView) {
		if (liveView) {
			return new MatrixVectorView(this, false);
		} else {
			int dimension = getDimension();
			double[][] elements = new double[dimension][1];
			
			for (int idx = 0; idx < dimension; idx++) {
				elements[idx][0] = get(idx);
			}
			
			return new Matrix(dimension, 1, elements, true);
		}
	}

	@Override
	public double[] toArray() {
		int dimension = getDimension();
		double[] array = new double[dimension];
		iterateVector((idx) -> array[idx] = get(idx));
		return array;
	}
	@Override
	public String toString() {
		return toString(3);
	}
	
	public String toString(int decimals) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int idx = 0, dimension = getDimension(); idx < dimension; idx++) {
			builder.append(String.format(" %."+ decimals + "f", get(idx)));
		}
		builder.append(" ]");
		return builder.toString();
	}
	
	private interface Action {
		void doAction(int idx);
	}
	
	private void iterateVector(Action action) {
		int dimension = this.getDimension();
		for (int idx = 0; idx < dimension; idx++) {
			action.doAction(idx);
		}
	}
}
