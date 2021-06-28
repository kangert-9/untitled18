package lesson13;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    private CountDownLatch cdl;
    private CyclicBarrier cb;
    private final static AtomicBoolean isWin = new AtomicBoolean();


    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            if (cb != null) {
                cb.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if (!isWin.getAndSet(true)) {
            System.out.println(this.name + " is winner");
        }
        cdl.countDown();
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.cb = barrier;
    }
    public void setFinishLatch(CountDownLatch finishLatch) {
        this.cdl = finishLatch;
    }
}
