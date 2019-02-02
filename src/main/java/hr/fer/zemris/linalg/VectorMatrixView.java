package hr.fer.zemris.linalg;


public class VectorMatrixView extends AbstractVector {

	private IMatrix original;
	private int dimension;
	private boolean rowMatrix;
	
	public VectorMatrixView(IMatrix original) {
		int rows = original.getRowsCount();
		int cols = original.getColsCount();

		this.rowMatrix = rows == 1;
		this.dimension = rows == 1 ? cols : rows;
		this.original = original;
	}
	
	@Override
	public double get(int index) {
		return original.get(rowMatrix ? 0 : index, rowMatrix ? index : 1);
	}

	@Override
	public IVector set(int index, double value) {
		original.set(rowMatrix ? 0 : index, rowMatrix ? index : 1, value);
		return this;
	}

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public IVector copy() {
		return new VectorMatrixView(original.copy());
	}

	@Override
	public IVector newInstance(int dimension) {
		return new VectorMatrixView(original.newInstance(rowMatrix ? 1 : dimension, rowMatrix ? dimension : 1));
	}

}
