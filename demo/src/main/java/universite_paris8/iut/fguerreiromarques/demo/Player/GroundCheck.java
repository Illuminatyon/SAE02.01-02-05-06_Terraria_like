package universite_paris8.iut.fguerreiromarques.demo.Player;

public class GroundCheck {
    public boolean isOnGround(double y, int playerHeight, int groundY) {
        return y + playerHeight >= groundY;
    }
}
