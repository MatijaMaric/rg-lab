package hr.fer.zemris.linalg;

import java.util.Arrays;
import java.util.Objects;

public class Vector extends AbstractVector {

	double[] elements;
	int dimension;
	boolean readOnly;

	public Vector(double[] elements) {
		this(false, false, elements);
	}

	public Vector(boolean readOnly, boolean useGiven, double[] elements) {
		this.readOnly = readOnly;
		
		if (useGiven) {
			this.elements = elements;
		} else {
			this.elements = Arrays.copyOf(elements, elements.length);
		}
		
		this.dimension = this.elements.length;
	}

    public static IVector defaultVector(int dimension) {
        return new Vector(new double[dimension]);
    }

    @Override
	public double get(int index) {
		return elements[index];
	}

	@Override
	public IVector set(int index, double value){
		elements[index] = value;
		return this;
	}

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public IVector copy() {
		return copyPart(dimension);
	}

	@Override
	public IVector newInstance(int dimension) {
		return defaultVector(dimension);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vector vector = (Vector) o;
		return dimension == vector.dimension &&
				Arrays.equals(elements, vector.elements);
	}

	@Override
	public int hashCode() {
		return Objects.hash(elements, dimension);
	}
}
