package com.srg;

import com.srg.Demo.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel {

    public static final int WIDTH = 380;
    public static final int HEIGHT = 650;
    private static BufferedImage background;
    public static BufferedImage bee;
    public static BufferedImage heroOne;
    public static BufferedImage heroTwo;
    public static BufferedImage airPlane;
    public static BufferedImage bullet;
    private static BufferedImage start;
    private static BufferedImage pause;
    private static BufferedImage gameOver;

    private Hero hero = new Hero();
    private Bullet[] bullets = {};
    private FlyObject[] flyObjects = {};
    private int score;
    private static int state;
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int PAUSE = 2;
    private static final int END = 3;

    static {

        try {
            background = ImageIO.read(Main.class.getResource("tool/background.png"));
            bee = ImageIO.read(Main.class.getResource("tool/bee.png"));
            heroOne = ImageIO.read(Main.class.getResource("tool/heroOne.png"));
            heroTwo = ImageIO.read(Main.class.getResource("tool/heroTwo.png"));
            airPlane = ImageIO.read(Main.class.getResource("tool/airPlane.png"));
            bullet = ImageIO.read(Main.class.getResource("tool/bullet.png"));
            start = ImageIO.read(Main.class.getResource("tool/start.png"));
            pause = ImageIO.read(Main.class.getResource("tool/pause.png"));
            gameOver = ImageIO.read(Main.class.getResource("tool/gameOver.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("飞机大战");
        Main main = new Main();
        jFrame.add(main);
        jFrame.setSize(Main.WIDTH, Main.HEIGHT);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        main.setStart();
    }

    private void setStart() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (state == RUNNING) {
                    int x = e.getX();
                    int y = e.getY();
                    hero.moveTo(x, y);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (state) {
                    case START:
                        state = RUNNING;
                        break;
                    case END:
                        hero = new Hero();
                        flyObjects = new FlyObject[0];
                        bullets = new Bullet[0];
                        score = 0;
                        state = START;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (state == PAUSE) {
                    state = RUNNING;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (state != END && state != START) {
                    state = PAUSE;
                }
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        int interval = 10;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) {
                    enterAction();
                    stepAction();
                    shootAction();
                    checkAction();
                    checkOutOfAction();
                    checkHeroAction();
                    checkGameOver();
                }
                repaint();
            }
        }, interval, interval);
    }

    private void checkOutOfAction() {
        for (int i = 0; i < flyObjects.length; i++) {
            if (flyObjects[i].isOutBound()) {
                FlyObject flyObject = flyObjects[i];
                flyObjects[i] = flyObjects[flyObjects.length - 1];
                flyObjects[flyObjects.length - 1] = flyObject;
                flyObjects = Arrays.copyOf(flyObjects, flyObjects.length - 1);
            }
        }

        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i].isOutBound()) {
                Bullet bullet = bullets[i];
                bullets[i] = bullets[bullets.length - 1];
                bullets[bullets.length - 1] = bullet;
                bullets = Arrays.copyOf(bullets, bullets.length - 1);
            }
        }
    }

    private void checkGameOver() {
        if (isOver()) {
            state = END;
        }
    }

    private boolean isOver() {
        return hero.getLife() <= 0;
    }

    private void checkHeroAction() {
        for (int i = 0; i < flyObjects.length; i++) {
            if (hero.hit(flyObjects[i])) {
                if (hero.getLife() > 0) {
                    hero.subLife();
                    hero.setDoubleFire(0);
                    FlyObject flyObject = flyObjects[i];
                    flyObjects[i] = flyObjects[flyObjects.length - 1];
                    flyObjects[flyObjects.length - 1] = flyObject;
                    flyObjects = Arrays.copyOf(flyObjects, flyObjects.length - 1);
                }
            }
        }
    }

    private void checkAction() {
        for (int i = 0; i < bullets.length; i++) {
            if (check(bullets[i])) {
                Bullet temp = bullets[i];
                bullets[i] = bullets[bullets.length - 1];
                bullets[bullets.length - 1] = temp;
                bullets = Arrays.copyOf(bullets, bullets.length - 1);

            }
        }
    }

    private boolean check(Bullet bullet) {
        int indexFly = -1;
        for (int i = 0; i < flyObjects.length; i++) {
            if (flyObjects[i].shootBy(bullet)) {
                indexFly = i;
                break;
            }
        }
        if (indexFly != -1) {
            FlyObject flyObject = flyObjects[indexFly];
            FlyObject flyObject1 = flyObjects[indexFly];
            flyObjects[indexFly] = flyObjects[flyObjects.length - 1];
            flyObjects[flyObjects.length - 1] = flyObject1;
            flyObjects = Arrays.copyOf(flyObjects, flyObjects.length - 1);
            if (flyObject instanceof AirPlane) {
                AirPlane airPlane = (AirPlane) flyObject;
                int sc = airPlane.getScore();
                score += sc;
            } else {
                Bee bee = (Bee) flyObject;
                int flag = bee.getType();
                if (flag == 0 && hero.getDoubleFire() != 1) {
                    hero.setDoubleFire(1);
                } else {
                    hero.sumLife();
                }
            }
            return true;
        }
        return false;
    }

    private void stepAction() {
        for (FlyObject flyObject : flyObjects) {
            flyObject.move();
        }
        hero.move();
        for (Bullet bullet : bullets) {
            bullet.move();
        }
    }

    private int shootIndex = 0;

    private void shootAction() {
        if (shootIndex++ % 5 == 0) {
            Bullet[] bs = hero.shoot();
            // 扩容
            bullets = Arrays.copyOf(bullets, bs.length + bullets.length);
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length,
                    bs.length);
            System.out.println("0000");
        }
    }

    private int index = 0;

    private void enterAction() {
        if (index++ % 10 == 0) {
            FlyObject flyObject = nextOne();
            flyObjects = Arrays.copyOf(flyObjects, flyObjects.length + 1);
            flyObjects[flyObjects.length - 1] = flyObject;
        }
    }

    private FlyObject nextOne() {
        Random random = new Random();
        int i = random.nextInt(10);
        if (i == 0) {
            return new Bee();
        } else {
            return new AirPlane();
        }
    }

    public void paint(Graphics graphics) {
        graphics.drawImage(background, 0, 0, null);
        graphics.setColor(Color.BLUE);
        graphics.setFont(new Font("楷体", 25, 15));
        graphics.drawString("分数：" + score, 15, 30);
        graphics.drawString("生命值：" + hero.getLife(), 15, 45);

        paintHero(graphics);
        paintBullet(graphics);
        paintFlyObject(graphics);
        paintState(graphics);
    }

    private void paintState(Graphics graphics) {
        switch (state) {
            case START:
                graphics.drawImage(start, 0, 0, null);
                break;
            case PAUSE:
                graphics.drawImage(pause, 0, 0, null);
                break;
            case END:
                graphics.drawImage(gameOver, 0, 0, null);
                break;
            default:
                break;
        }
    }

    private void paintFlyObject(Graphics graphics) {
        for (FlyObject flyObject : flyObjects) {
            if (flyObject instanceof AirPlane) {
                AirPlane airPlane = (AirPlane) flyObject;
                graphics.drawImage(airPlane.image, airPlane.x, airPlane.y, null);
            } else if (flyObject instanceof Bee) {
                Bee bee = (Bee) flyObject;
                graphics.drawImage(bee.image, bee.x, bee.y, null);
            }
        }
    }

    private void paintBullet(Graphics graphics) {
        for (Bullet bullet : bullets) {
            graphics.drawImage(bullet.image, bullet.x, bullet.y, null);
        }
    }

    private void paintHero(Graphics graphics) {
        graphics.drawImage(hero.image, hero.x, hero.y, null);
    }


}