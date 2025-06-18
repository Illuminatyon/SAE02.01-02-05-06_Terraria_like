package fr.iut.hev.root.model.pathfinding;

/**
 * Helper class for A* algorithm
 */
public class AStarNode {
    public Point point;
    public int fScore;

    public AStarNode(Point point, int fScore) {
        this.point = point;
        this.fScore = fScore;
    }
}