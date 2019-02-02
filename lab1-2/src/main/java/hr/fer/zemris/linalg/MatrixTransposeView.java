package hr.fer.zemris.linalg;

public class MatrixTransposeView extends AbstractMatrix {

	private IMatrix original;
	
	public MatrixTransposeView(IMatrix original) {
		this.original = original;
	}
	
	@Override
	public int getRowsCount() {
		return original.getColsCount();
	}

	@Override
	public int getColsCount() {
		return original.getRowsCount();
	}

	@Override
	public double get(int row, int col) {
		return original.get(col, row);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		return original.set(col, row, value);
	}

	@Override
	public IMatrix copy() {
		return new MatrixTransposeView(original.copy());
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return original.newInstance(rows, cols).nTranspose(true);
	}

}
