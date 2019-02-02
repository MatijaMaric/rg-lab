package hr.fer.zemris.graphics.common;

import hr.fer.zemris.linalg.IVector;
import hr.fer.zemris.linalg.Vector;

public class Vertex3D {
    public double x;
    public double y;
    public double z;

    private IVector normal;

    public Vertex3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public IVector vector() {
        return new Vector(new double[]{x, y, z});
    }

    public Vertex3D moveBy(IVector vector) {
        this.x += vector.get(0);
        this.y += vector.get(1);
        this.z += vector.get(2);
        return this;
    }

    public Vertex3D scale(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public void setNormal(IVector normal) {
        this.normal = normal;
    }

    public IVector getNormal() {
        return normal;
    }

    public Vertex3D copy() {
        return new Vertex3D(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex3D vertex3D = (Vertex3D) o;

        if (Double.compare(vertex3D.x, x) != 0) return false;
        if (Double.compare(vertex3D.y, y) != 0) return false;
        return Double.compare(vertex3D.z, z) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
