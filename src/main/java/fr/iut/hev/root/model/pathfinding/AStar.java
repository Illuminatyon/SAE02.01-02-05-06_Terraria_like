package fr.iut.hev.root.model.pathfinding;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypesEnum;

import java.util.*;

/**
 * Implementation of the A* pathfinding algorithm
 */
public class AStar {
    private final TileMap tileMap;

    public AStar(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    /**
     * Finds the shortest path from start to target using A* algorithm
     * @param startX starting X position in tile coordinates
     * @param startY starting Y position in tile coordinates
     * @param targetX target X position in tile coordinates
     * @param targetY target Y position in tile coordinates
     * @return List of Points representing the path, or empty list if no path found
     */
    public List<Point> findPath(int startX, int startY, int targetX, int targetY) {
        // Create start and target points
        Point start = new Point(startX, startY);
        Point target = new Point(targetX, targetY);

        // If we're already at the target, no need to find a path
        if (start.equals(target)) {
            return new ArrayList<>();
        }

        // A* data structures
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Integer> gScore = new HashMap<>(); // Cost from start to current node
        Map<Point, Integer> fScore = new HashMap<>(); // Estimated total cost from start to goal through current node
        Set<Point> closedSet = new HashSet<>();

        // Initialize scores
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, target));
        openSet.add(new AStarNode(start, fScore.get(start)));

        // Directions: up, right, down, left, up-right, up-left, down-right, down-left
        int[] dx = {0, 1, 0, -1, 1, -1, 1, -1};
        int[] dy = {-1, 0, 1, 0, -1, -1, 1, 1};

        boolean foundPath = false;

        while (!openSet.isEmpty()) {
            Point current = openSet.poll().point;

            // Check if we've reached the target
            if (current.equals(target)) {
                foundPath = true;
                break;
            }

            closedSet.add(current);

            // Try all directions
            for (int i = 0; i < dx.length; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];
                Point neighbor = new Point(newX, newY);

                // Skip if already evaluated or not valid
                if (closedSet.contains(neighbor) || !isValidPosition(newX, newY)) {
                    continue;
                }

                // For diagonal movement, check if both adjacent tiles are valid
                if (i >= 4) { // Diagonal movement
                    int adjacentX = current.x + dx[i % 4]; // Get the horizontal component
                    int adjacentY = current.y + dy[(i + 2) % 4]; // Get the vertical component

                    // If either adjacent tile is not valid, skip this diagonal
                    if (!isValidPosition(adjacentX, current.y) || !isValidPosition(current.x, adjacentY)) {
                        continue;
                    }
                }

                // Calculate tentative gScore
                int moveCost = (i < 4) ? 10 : 14; // Straight = 10, Diagonal = 14 (approximation of sqrt(2) * 10)
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + moveCost;

                // If this path is better than any previous one
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    // Record this path
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    int estimatedFScore = tentativeGScore + heuristic(neighbor, target);
                    fScore.put(neighbor, estimatedFScore);

                    // Add to open set if not already there
                    boolean found = false;
                    for (AStarNode node : openSet) {
                        if (node.point.equals(neighbor)) {
                            found = true;
                            node.fScore = estimatedFScore;
                            break;
                        }
                    }
                    if (!found) {
                        openSet.add(new AStarNode(neighbor, estimatedFScore));
                    }
                }
            }

            // Special case for jumping (up to 2 tiles high)
            for (int jumpHeight = 1; jumpHeight <= 2; jumpHeight++) {
                // Try jumping up
                int jumpX = current.x;
                int jumpY = current.y - jumpHeight;
                Point jumpPoint = new Point(jumpX, jumpY);

                // Skip if already evaluated or not valid
                if (closedSet.contains(jumpPoint) || jumpY < 0 || !isValidPosition(jumpX, jumpY)) {
                    continue;
                }

                // Calculate tentative gScore (jumping costs more)
                int jumpCost = 15 * jumpHeight; // Jumping costs more than walking
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + jumpCost;

                // If this path is better than any previous one
                if (tentativeGScore < gScore.getOrDefault(jumpPoint, Integer.MAX_VALUE)) {
                    // Record this path
                    cameFrom.put(jumpPoint, current);
                    gScore.put(jumpPoint, tentativeGScore);
                    int estimatedFScore = tentativeGScore + heuristic(jumpPoint, target);
                    fScore.put(jumpPoint, estimatedFScore);

                    // Add to open set if not already there
                    boolean found = false;
                    for (AStarNode node : openSet) {
                        if (node.point.equals(jumpPoint)) {
                            found = true;
                            node.fScore = estimatedFScore;
                            break;
                        }
                    }
                    if (!found) {
                        openSet.add(new AStarNode(jumpPoint, estimatedFScore));
                    }
                }
            }
        }

        // Reconstruct the path if found
        List<Point> path = new ArrayList<>();
        if (foundPath) {
            Point current = target;
            while (!current.equals(start)) {
                path.add(0, current); // Add to the beginning of the list
                current = cameFrom.get(current);
            }
        }

        return path;
    }

    /**
     * Calculates the heuristic (estimated distance) between two points
     * Uses Manhattan distance for A* algorithm
     */
    private int heuristic(Point a, Point b) {
        // Manhattan distance
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * Checks if a position is valid (within bounds and passable)
     */
    public boolean isValidPosition(int x, int y) {
        // Check if position is within bounds
        if (x < 0 || y < 0 || x >= tileMap.getWidth() || y >= tileMap.getHeight()) {
            return false;
        }

        // Check if the tile is passable (AIR)
        return tileMap.getTile(x, y).getTileEnum().getType() == TileTypesEnum.AIR;
    }
}
