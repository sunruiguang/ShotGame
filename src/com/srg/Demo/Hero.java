package com.srg.Demo;

import com.srg.Main;

import java.awt.image.BufferedImage;

public class Hero extends FlyObject {

    private int life;
    private int doubleFire;
    private BufferedImage[] images;

    public Hero() {
        life = 3;
        doubleFire = 0;
        images = new BufferedImage[]{Main.heroOne, Main.heroTwo};
        image = Main.heroOne;

        width = image.getWidth();
        height = image.getHeight();

        x = 150;
        y = 450;
    }

    public Bullet[] shoot() {
        int xStep = width / 4;
        Bullet[] bullets;
        if (doubleFire == 0) {
            bullets = new Bullet[1];
            bullets[0] = new Bullet(x + xStep * 2, y);
        } else {
            bullets = new Bullet[5];
            bullets[0] = new Bullet(x + xStep, y);
            bullets[1] = new Bullet(x + xStep * 3, y);
            bullets[2] = new Bullet(x + xStep * 4, y);
            bullets[3] = new Bullet(x, y);
            bullets[4] = new Bullet(x + xStep * 2, y);
        }
        return bullets;
    }

    public int getLife() {
        return life;
    }

    public int getDoubleFire() {
        return doubleFire;
    }

    public void setDoubleFire(int doubleFire) {
        this.doubleFire = doubleFire;
    }

    private int index = 0;

    @Override
    public void move() {
        int num = index++ / 10 % images.length;
        image = images[num];
    }

    public void moveTo(int x, int y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }

    @Override
    public boolean isOutBound() {
        return false;
    }

    public boolean hit(FlyObject flyObject) {
        int heroX = this.x + width / 2;
        int heroY = this.y + height / 2;
        int x1 = flyObject.x - width / 2;
        int x2 = flyObject.x + flyObject.width + width / 2;
        int y1 = flyObject.y - height / 2;
        int y2 = flyObject.y + flyObject.height + height / 2;

        return heroX > x1 && heroX < x2 && heroY > y1 && heroY < y2;
    }

    public void subLife() {
        life -= 0;

    }

    public void sumLife() {
        life++;
    }
}
