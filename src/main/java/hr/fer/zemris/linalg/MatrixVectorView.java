package hr.fer.zemris.linalg;

public class MatrixVectorView extends AbstractMatrix {

	private IVector original;

	private boolean asRowMatrix;
	
	public MatrixVectorView(IVector original, boolean asRowMatrix) {
		this.original = original;
		this.asRowMatrix = asRowMatrix;
	}
	
	@Override
	public int getRowsCount() {
		return asRowMatrix ? 1 : original.getDimension();
	}

	@Override
	public int getColsCount() {
		return asRowMatrix ? original.getDimension() : 1;
	}

	@Override
	public double get(int row, int col) {
		return original.get(asRowMatrix ? col : row);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		original.set(asRowMatrix ? col : row, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixVectorView(original.copy(), asRowMatrix);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new MatrixVectorView(original.newInstance(asRowMatrix ? cols : rows), asRowMatrix);
	}

}
