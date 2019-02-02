package hr.fer.zemris.graphics.common;

import hr.fer.zemris.linalg.IVector;
import hr.fer.zemris.linalg.Vector;

import java.util.*;

public class ObjectModel {
    Vertex3D[] vertices;
    Face3D[] faces;

    public ObjectModel(Vertex3D[] vertices, Face3D[] faces) {
        this.vertices = vertices;
        this.faces = faces;
    }

    public Face3D[] getFaces() {
        return faces;
    }

    public Vertex3D[] getVertices() {
        return vertices;
    }

    public ObjectModel copy() {
        Vertex3D[] verticesCopy = Arrays.copyOf(vertices, vertices.length);
        Face3D[] facesCopy = Arrays.copyOf(faces, faces.length);
        return new ObjectModel(verticesCopy, facesCopy);
    }

    public void normalize() {
        double xmin, xmax, ymin, ymax, zmin, zmax;
        xmin = xmax = vertices[0].x;
        ymin = ymax = vertices[0].y;
        zmin = zmax = vertices[0].z;

        for (Vertex3D v : vertices) {
            if (v.x > xmax) xmax = v.x;
            if (v.x < xmin) xmin = v.x;
            if (v.y > ymax) ymax = v.y;
            if (v.y < ymin) ymin = v.y;
            if (v.z > zmax) zmax = v.z;
            if (v.z < zmin) zmin = v.z;
        }
        double maxSpan = Math.max(xmax - xmin, Math.max(ymax - ymin, zmax - zmin));

        double xavg = (xmin + xmax) / 2;
        double yavg = (ymin + ymax) / 2;
        double zavg = (zmin + zmax) / 2;

        IVector vector = new Vector(new double[]{-xavg, -yavg, -zavg});
        double scaleFactor = 2/maxSpan;
        for (Vertex3D v : vertices) {
            v.moveBy(vector);
            v.scale(scaleFactor, scaleFactor, scaleFactor);
        }
    }

    public void computeVertexNormals() {
        Map<Vertex3D, List<IVector>> vertexNormals = new HashMap<>();
        for (Face3D face : faces) {
            for (Vertex3D vert : face.getVertices()) {
                vertexNormals.merge(vert, new ArrayList<>(), (o, n) -> { o.add(face.getNormal()); return o;});
            }
        }
        for (Vertex3D vert : vertexNormals.keySet()) {
            List<IVector> normals = vertexNormals.get(vert);
            vert.setNormal(getAverage(normals));
        }
    }

    private static IVector getAverage(List<IVector> vectors) {
        int length = vectors.get(0).getDimension();
        IVector result = new Vector(new double[length]);
        for (IVector v : vectors) {
            result.add(v);
        }
        result.scalarMultiply(1f/vectors.size());
        return result;
    }

}
