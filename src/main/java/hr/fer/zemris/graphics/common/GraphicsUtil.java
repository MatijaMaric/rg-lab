package hr.fer.zemris.graphics.common;

import hr.fer.zemris.curve.CubicBSpline;
import hr.fer.zemris.linalg.*;

import java.util.ArrayList;
import java.util.List;

public class GraphicsUtil {
    public static ObjectModel parseOBJ(List<String> OBJlines) {
        List<Vertex3D> vertices = new ArrayList<>();
        List<Face3D> faces = new ArrayList<>();
        for (String line : OBJlines) {
            if (line.startsWith("v")) {
                String[] lineParts = line.split(" ");
                double x = Double.parseDouble(lineParts[1]);
                double y = Double.parseDouble(lineParts[2]);
                double z = Double.parseDouble(lineParts[3]);
                vertices.add(new Vertex3D(x, y, z));
            } else if (line.startsWith("f")) {
                String[] lineParts = line.split(" ");
                int first = Integer.parseInt(lineParts[1]);
                int second = Integer.parseInt(lineParts[2]);
                int third = Integer.parseInt(lineParts[3]);
                Face3D face = new Face3D(vertices.get(first-1), vertices.get(second-1), vertices.get(third-1));
                face.setIndexes(new int[]{first, second, third});
                faces.add(face);
            }
        }

        Vertex3D[] vArr = vertices.toArray(new Vertex3D[vertices.size()]);
        Face3D[] fArr = faces.toArray(new Face3D[faces.size()]);
        return new ObjectModel(vArr, fArr);
    }

    public static CubicBSpline parseCubicBSpline(List<String> BSplineLines) {
        List<Vertex3D> points = new ArrayList<>();
        for (String line : BSplineLines) {
            String[] lineParts = line.trim().split(" ");
            double x = Double.parseDouble(lineParts[0]);
            double y = Double.parseDouble(lineParts[1]);
            double z = Double.parseDouble(lineParts[2]);
            points.add(new Vertex3D(x, y, z));
        }
        return new CubicBSpline(points);
    }


    public static IMatrix fromVectorList(List<IVector> rows) {
        int rowCount = rows.size();
        int colCount = rows.get(0).getDimension();
        IMatrix m = Matrix.defaultMatrix(rowCount, colCount);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                m.set(i, j, rows.get(i).get(j));
            }
        }
        return m;
    }
}
