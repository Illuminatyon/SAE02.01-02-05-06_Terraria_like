import java.awt.*;

public class Enemy {

    int x, y, width, heigth, lifePoint;
    boolean isAlive = true;

    public Enemy(int x, int y){
        this.x = x;
        this.y = y;
        this.width = 30;
        this.heigth = 50;
        this.lifePoint = 5;
    }

    public void draw(Graphics g){
        if (isAlive){
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, heigth);
        }
    }

    public int getLifePoint(){
        return this.lifePoint;
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, heigth);
    }

    public void hit() {
        this.lifePoint--;
        if (this.getLifePoint() == 0){
            this.isAlive = false;
        }
    }
}
