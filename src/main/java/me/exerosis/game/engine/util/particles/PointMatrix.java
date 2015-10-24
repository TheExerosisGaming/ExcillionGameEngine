package me.exerosis.game.engine.util.particles;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Exerosis
 */
public class PointMatrix implements Iterable<Vector> {
    private LinkedHashSet<Vector> _points = new LinkedHashSet<Vector>();

    /**
     * @param numberOfPoints - The number of points that will be found between vectors.
     * @param vectors        - The path that the points will be found between.
     */
    public PointMatrix(int numberOfPoints, Vector... vectors) {
        if (vectors.length < 2)
            throw new IllegalArgumentException("Must imput at least two vectors!");
        int pointsPerLine = numberOfPoints / (vectors.length / 2);

        for (int i = 1; i < vectors.length; i++)
            addPoints(vectors[i - 1], vectors[i], pointsPerLine);
    }

    /**
     * Can be used to increase the number of points or make shapes that need connected lines.
     *
     * @param numberOfPoints
     * @param pointIterator
     */
    public PointMatrix(int numberOfPoints, PointMatrix pointMatrix) {
        this(numberOfPoints, (Vector[]) pointMatrix.getPoints().toArray());
    }

    private void addPoints(Vector start, Vector end, int numberOfPoints) {
        double x = start.getX(), y = start.getY(), z = start.getZ();
        Vector difference = start.subtract(end);
        double xIncrement = (difference.getX() / numberOfPoints);
        double yIncrement = (difference.getY() / numberOfPoints);
        double zIncrement = (difference.getZ() / numberOfPoints);
        for (int i = 0; i < numberOfPoints; i++)
            _points.add(new Vector(x -= xIncrement, y -= yIncrement, z -= zIncrement));
    }

    /**
     * @return a LinkedHashSet that contains all the points found.
     */
    public LinkedHashSet<Vector> getPointsLinked() {
        return _points;
    }

    /**
     * @return a List that contains all the points found.
     */
    public List<Vector> getPoints() {
        return new ArrayList<Vector>(_points);
    }

    @Override
    public Iterator<Vector> iterator() {
        return _points.iterator();
    }
}
