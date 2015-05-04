/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import specifications.DataService;
import specifications.EngineService;
import specifications.PhantomService;
import specifications.RequireDataService;
import tools.HardCodedParameters;
import tools.Position;
import tools.User;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Engine implements EngineService, RequireDataService {
    private Timer engineClock;
    private DataService data;
    private User.COMMAND command;
    private Random gen;

    public Engine() {
    }

    @Override
    public void bindDataService(DataService service) {
        data = service;
    }

    @Override
    public void init() {
        engineClock = new Timer();
        command = User.COMMAND.NONE;
        gen = new Random();
    }

    @Override
    public void start() {
        engineClock.schedule(new TimerTask() {
            public void run() {
                System.out.println("Game step #" + data.getStepNumber() + ": checked.");

                if (gen.nextInt(20) == 0) spawnPhantom();

                for (PhantomService p : data.getPhantoms()) {
                    if (p.getAction() == PhantomService.MOVE.LEFT) moveLeft(p);
                    if (p.getAction() == PhantomService.MOVE.RIGHT) moveRight(p);
                    if (p.getAction() == PhantomService.MOVE.UP) moveUp(p);
                    if (p.getAction() == PhantomService.MOVE.DOWN) moveDown(p);
                }

                if (command == User.COMMAND.LEFT) heroesMoveLeft();
                if (command == User.COMMAND.RIGHT) heroesMoveRight();
                if (command == User.COMMAND.UP) heroesMoveUp();
                if (command == User.COMMAND.DOWN) heroesMoveDown();
                command = User.COMMAND.NONE;

                data.setStepNumber(data.getStepNumber() + 1);
            }
        }, 0, HardCodedParameters.enginePaceMillis);
    }

    @Override
    public void stop() {
        engineClock.cancel();
    }

    @Override
    public void setHeroesCommand(User.COMMAND c) {
        command = c;
    }

    private void heroesMoveLeft() {
        data.setHeroesPosition(new Position(data.getHeroesPosition().x - 10, data.getHeroesPosition().y));
    }

    private void heroesMoveRight() {
        data.setHeroesPosition(new Position(data.getHeroesPosition().x + 10, data.getHeroesPosition().y));
    }

    private void heroesMoveUp() {
        data.setHeroesPosition(new Position(data.getHeroesPosition().x, data.getHeroesPosition().y - 10));
    }

    private void heroesMoveDown() {
        data.setHeroesPosition(new Position(data.getHeroesPosition().x, data.getHeroesPosition().y + 10));
    }

    private void spawnPhantom() {
        int x = (int) (HardCodedParameters.defaultWidth * .9);
        int y = 0;
        boolean cont = true;
        while (cont) {
            y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6)) + HardCodedParameters.defaultHeight * .1);
            cont = false;
            for (PhantomService p : data.getPhantoms()) {
                if (p.getPosition().equals(new Position(x, y))) cont = true;
            }
        }
        data.addPhantom(new Position(x, y));
    }

    private void moveLeft(PhantomService p) {
        p.setPosition(new Position(p.getPosition().x - 10, p.getPosition().y));
    }

    private void moveRight(PhantomService p) {
        p.setPosition(new Position(p.getPosition().x + 10, p.getPosition().y));
    }

    private void moveUp(PhantomService p) {
        p.setPosition(new Position(p.getPosition().x, p.getPosition().y - 10));
    }

    private void moveDown(PhantomService p) {
        p.setPosition(new Position(p.getPosition().x, p.getPosition().y + 10));
    }
}
