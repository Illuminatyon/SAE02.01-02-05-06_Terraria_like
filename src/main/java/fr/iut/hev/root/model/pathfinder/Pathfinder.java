package fr.iut.hev.root.model.pathfinder;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.Tiles;
import fr.iut.hev.root.model.Tile;
import java.util.*;
import java.util.function.Predicate;

public class Pathfinder {
    private final TileMap tileMap;
    private final int maxJumpHeight;
    private final int maxHorizontalMove;
    private final Set<String> solidTilesCache = new HashSet<>();

    public Pathfinder(TileMap tileMap, int maxJumpHeight, int maxHorizontalMove) {
        this.tileMap = tileMap;
        this.maxJumpHeight = maxJumpHeight;
        this.maxHorizontalMove = maxHorizontalMove;
    }

    public static class PathPoint {
        public int x, y;
        public double g; // Coût depuis le départ
        public double h; // Heuristique
        public PathPoint parent;
        public boolean isJumping;
        public boolean isFalling;
        public int jumpHeight;

        public PathPoint(int x, int y) {
            this.x = x;
            this.y = y;
            this.g = 0;
            this.h = 0;
            this.parent = null;
            this.isJumping = false;
            this.isFalling = false;
            this.jumpHeight = 0;
        }

        public double getF() {
            return g + h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathPoint pathPoint = (PathPoint) o;
            return x == pathPoint.x && y == pathPoint.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    public List<PathPoint> findPath(int startX, int startY, int goalX, int goalY) {
        System.out.println("\n=== DEBUT PATHFINDING ===");
        System.out.println("De (" + startX + "," + startY + ") à (" + goalX + "," + goalY + ")");

        List<PathPoint> path = new ArrayList<>();
        PathPoint start = new PathPoint(startX, startY);
        PathPoint goal = new PathPoint(goalX, goalY);

        // Vérification des positions de départ et d'arrivée
        if (!isValidPosition(startX, startY) || !isValidPosition(goalX, goalY)) {
            System.out.println("Invalid start or goal position");
            return path;
        }

        if (startX == goalX && startY == goalY) {
            System.out.println("Déjà à destination");
            return path;
        }

        // Vérifier si la position de départ ou d'arrivée est solide
        if (isSolid(startX, startY)) {
            System.out.println("Position de départ est un obstacle!");
            return path;
        }
        if (isSolid(goalX, goalY)) {
            System.out.println("Position d'arrivée est un obstacle!");
            return path;
        }

        PriorityQueue<PathPoint> openSet = new PriorityQueue<>(Comparator.comparingDouble(PathPoint::getF));
        Set<PathPoint> closedSet = new HashSet<>();

        start.g = 0;
        start.h = calculateHeuristic(start, goal);
        openSet.add(start);

        int iterations = 0;
        int maxIterations = 1000; // Limite pour éviter les boucles infinies
        boolean pathFound = false;

        while (!openSet.isEmpty() && iterations < maxIterations) {
            iterations++;
            PathPoint current = openSet.poll();

            // Vérifier si on a atteint la destination
            if (current.x == goal.x && current.y == goal.y) {
                pathFound = true;
                reconstructPath(current, path);
                System.out.println("Chemin trouvé en " + iterations + " itérations");
                System.out.println("Chemin: " + path.size() + " points");
                break;
            }

            closedSet.add(current);

            // Essayer de marcher
            tryWalking(current, goal, openSet, closedSet);

            // Essayer de sauter
            tryJumping(current, goal, openSet, closedSet);

            // Essayer de tomber
            tryFalling(current, goal, openSet, closedSet);
        }

        if (!pathFound) {
            System.out.println("Aucun chemin trouvé après " + iterations + " itérations");

            // Essayer de trouver pourquoi
            if (isSolid(goalX, goalY)) {
                System.out.println("La destination est un obstacle");
            }

            // Vérifier si la destination est accessible
            if (!isAccessible(goalX, goalY, startX, startY)) {
                System.out.println("La destination n'est pas accessible depuis le point de départ");
            }

            // Retourner un chemin vide
            return new ArrayList<>();
        }

        return path;
    }

    private boolean isAccessible(int x, int y, int startX, int startY) {
        // Vérifier si la position est valide
        if (!isValidPosition(x, y)) return false;

        // Vérifier si la position est solide
        if (isSolid(x, y)) return false;

        // Pour simplifier, nous assumons que c'est accessible
        // Une vérification plus complète serait nécessaire
        return true;
    }

    private void tryWalking(PathPoint current, PathPoint goal,
                            PriorityQueue<PathPoint> openSet, Set<PathPoint> closedSet) {
        // Essayer de se déplacer horizontalement
        for (int dx = -maxHorizontalMove; dx <= maxHorizontalMove; dx++) {
            if (dx == 0) continue; // Pas besoin de rester sur place
            int nx = current.x + dx;
            int ny = current.y;

            if (canWalk(current, nx, ny)) {
                processNeighbor(current, nx, ny, openSet, closedSet, goal, false, false, 0);
            }
        }
    }

    private void tryJumping(PathPoint current, PathPoint goal,
                            PriorityQueue<PathPoint> openSet, Set<PathPoint> closedSet) {
        // Essayer de sauter - uniquement vers le haut ou horizontalement
        for (int dx = -maxHorizontalMove; dx <= maxHorizontalMove; dx++) {
            for (int dy = -maxJumpHeight; dy <= 0; dy++) {
                if (dx == 0 && dy == 0) continue; // Pas besoin de rester sur place

                int nx = current.x + dx;
                int ny = current.y + dy;

                if (canJump(current, nx, ny)) {
                    int newJumpHeight = current.isJumping ? current.jumpHeight + 1 : 1;
                    processNeighbor(current, nx, ny, openSet, closedSet, goal, true, false, newJumpHeight);
                }
            }
        }
    }

    private void tryFalling(PathPoint current, PathPoint goal,
                            PriorityQueue<PathPoint> openSet, Set<PathPoint> closedSet) {
        // Essayer de tomber - seulement vers le bas
        int nx = current.x;
        int ny = current.y + 1; // Tomber vers le bas

        if (canFall(current, nx, ny)) {
            processNeighbor(current, nx, ny, openSet, closedSet, goal, false, true, 0);
        }
    }

    private void processNeighbor(PathPoint current, int nx, int ny,
                                 PriorityQueue<PathPoint> openSet, Set<PathPoint> closedSet,
                                 PathPoint goal, boolean isJumping, boolean isFalling, int jumpHeight) {
        PathPoint neighbor = new PathPoint(nx, ny);
        neighbor.isJumping = isJumping;
        neighbor.isFalling = isFalling;
        neighbor.jumpHeight = jumpHeight;

        // Vérifier si le voisin est déjà dans closedSet
        if (isInClosedSet(neighbor, closedSet)) {
            return;
        }

        // Calculer le coût du mouvement
        double moveCost = 1.0;
        if (isJumping) moveCost = 1.5; // Sauter coûte plus cher
        if (isFalling) moveCost = 0.5; // Tomber coûte moins cher

        // Calculer le coût potentiel
        double tentativeG = current.g + moveCost;

        // Vérifier si le voisin est déjà dans openSet
        PathPoint existingNeighbor = null;
        for (PathPoint p : openSet) {
            if (p.equals(neighbor)) {
                existingNeighbor = p;
                break;
            }
        }

        // Si le voisin n'est pas dans openSet ou que ce nouveau chemin est meilleur
        if (existingNeighbor == null || tentativeG < existingNeighbor.g) {
            PathPoint neighborToUse = existingNeighbor != null ? existingNeighbor : neighbor;

            neighborToUse.parent = current;
            neighborToUse.g = tentativeG;
            neighborToUse.h = calculateHeuristic(neighborToUse, goal);
            neighborToUse.isJumping = isJumping;
            neighborToUse.isFalling = isFalling;
            neighborToUse.jumpHeight = jumpHeight;

            if (existingNeighbor == null) {
                openSet.add(neighborToUse);
            }
        }
    }

    private boolean isInClosedSet(PathPoint point, Set<PathPoint> closedSet) {
        for (PathPoint p : closedSet) {
            if (p.equals(point)) {
                return true;
            }
        }
        return false;
    }

    private double calculateHeuristic(PathPoint point, PathPoint goal) {
        // Distance de Manhattan
        return Math.abs(point.x - goal.x) + Math.abs(point.y - goal.y);
    }

    private void reconstructPath(PathPoint goal, List<PathPoint> path) {
        path.clear();
        PathPoint current = goal;
        while (current != null) {
            path.add(0, current); // Insérer au début pour avoir l'ordre correct
            current = current.parent;
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < tileMap.getWidth() && y >= 0 && y < tileMap.getHeight();
    }

    private boolean isSolid(int x, int y) {
        String key = x + "," + y;
        if (solidTilesCache.contains(key)) {
            return true;
        }

        if (!isValidPosition(x, y)) {
            return true; // Hors limites est considéré comme solide
        }

        Tile tile = tileMap.getTile(x, y);
        if (tile == null) return false;

        try {
            Object tileObj = tile.getTile();
            if (tileObj instanceof Tiles) {
                Tiles tileType = (Tiles) tileObj;
                boolean isSolid = tileType.getType().getHasCollision();
                if (isSolid) {
                    solidTilesCache.add(key);
                }
                return isSolid;
            }
        } catch (Exception e) {
            System.err.println("Error checking if tile is solid at (" + x + ", " + y + "): " + e.getMessage());
        }
        return true; // Par défaut, considérer comme solide
    }

    private boolean hasGroundBeneath(int x, int y) {
        // Vérifier si il y a un sol sous la position (x,y)
        // C'est-à-dire si la tuile en dessous est solide
        return isSolid(x, y + 1);
    }

    private boolean canWalk(PathPoint from, int toX, int toY) {
        if (!isValidPosition(toX, toY)) return false;

        // On ne peut marcher que sur le même niveau
        if (toY != from.y) return false;

        // Doit avoir un sol sous les pieds
        if (!hasGroundBeneath(toX, toY)) return false;

        // La case cible doit être libre
        return !isSolid(toX, toY);
    }

    private boolean canJump(PathPoint from, int toX, int toY) {
        if (!isValidPosition(toX, toY)) return false;

        // Doit avoir du sol sous les pieds pour sauter (sauf si déjà en train de sauter)
        if (!from.isJumping && !hasGroundBeneath(from.x, from.y)) return false;

        // Limite de hauteur de saut
        int heightDifference = from.y - toY;
        if (from.isJumping) {
            if (from.jumpHeight >= maxJumpHeight) return false;
            if (heightDifference > 1) return false; // Ne peut sauter que d'une tuile à la fois
        } else {
            if (heightDifference > maxJumpHeight) return false;
        }

        // Vérifier qu'il n'y a pas d'obstacle sur le chemin du saut
        if (from.x != toX) { // Saut horizontal
            int stepX = toX > from.x ? 1 : -1;
            for (int x = from.x; x != toX; x += stepX) {
                if (isSolid(x, toY)) return false;
            }
        }

        // La case cible doit être libre
        return !isSolid(toX, toY);
    }

    private boolean canFall(PathPoint from, int toX, int toY) {
        if (!isValidPosition(toX, toY)) return false;

        // Doit tomber vers le bas (toY > from.y)
        if (toY <= from.y) return false;

        // Doit ne pas avoir de sol sous les pieds pour tomber
        if (hasGroundBeneath(from.x, from.y)) return false;

        // Vérifier que la chute ne passe pas à travers un sol
        // On vérifie chaque case entre from.y et toY
        for (int y = from.y + 1; y <= toY; y++) {
            if (isSolid(from.x, y)) return false;
        }

        // La case cible doit être libre
        return !isSolid(toX, toY);
    }

    /**
     * Méthode de débogage pour afficher la carte avec les obstacles
     */
    public void debugPrintMap() {
        System.out.println("\n=== DEBUG MAP ===");
        System.out.println("TileMap dimensions: " + tileMap.getWidth() + "x" + tileMap.getHeight());

        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                boolean solid = isSolid(x, y);
                System.out.print(solid ? "X" : ".");
            }
            System.out.println();
        }
        System.out.println("=== END DEBUG ===\n");
    }
}
