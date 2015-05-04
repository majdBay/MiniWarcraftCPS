/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: userInterface/Viewer.java 2015-03-11 buixuan.
 * ******************************************************/
package userInterface;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import specifications.PhantomService;
import specifications.ReadService;
import specifications.RequireReadService;
import specifications.ViewerService;
import tools.HardCodedParameters;

import java.util.ArrayList;

public class Viewer implements ViewerService, RequireReadService {
    private static final int spriteSlowDownRate = HardCodedParameters.spriteSlowDownRate;
    private ReadService data;
    private ImageView heroesAvatar;
    private Image heroesSpriteSheet;
    private ArrayList<Rectangle2D> heroesAvatarViewports;
    private ArrayList<Integer> heroesAvatarXModifiers;
    private ArrayList<Integer> heroesAvatarYModifiers;
    private int heroesAvatarViewportIndex;

    public Viewer() {
    }

    @Override
    public void bindReadService(ReadService service) {
        data = service;
    }

    @Override
    public void init() {
        //Yucky hard-conding
        heroesSpriteSheet = new Image("file:src/images/modern soldier large.png");
        heroesAvatar = new ImageView(heroesSpriteSheet);
        heroesAvatarViewports = new ArrayList<Rectangle2D>();
        heroesAvatarXModifiers = new ArrayList<Integer>();
        heroesAvatarYModifiers = new ArrayList<Integer>();

        heroesAvatarViewportIndex = 0;

        //heroesAvatarViewports.add(new Rectangle2D(301,386,95,192));
        heroesAvatarViewports.add(new Rectangle2D(570, 194, 115, 190));
        heroesAvatarViewports.add(new Rectangle2D(398, 386, 133, 192));
        heroesAvatarViewports.add(new Rectangle2D(155, 194, 147, 190));
        heroesAvatarViewports.add(new Rectangle2D(785, 386, 127, 194));
        heroesAvatarViewports.add(new Rectangle2D(127, 582, 135, 198));
        heroesAvatarViewports.add(new Rectangle2D(264, 582, 111, 200));
        heroesAvatarViewports.add(new Rectangle2D(2, 582, 123, 198));
        heroesAvatarViewports.add(new Rectangle2D(533, 386, 115, 192));
        //heroesAvatarViewports.add(new Rectangle2D(204,386,95,192));

        //heroesAvatarXModifiers.add(10);heroesAvatarYModifiers.add(-7);
        heroesAvatarXModifiers.add(6);
        heroesAvatarYModifiers.add(-6);
        heroesAvatarXModifiers.add(2);
        heroesAvatarYModifiers.add(-8);
        heroesAvatarXModifiers.add(1);
        heroesAvatarYModifiers.add(-10);
        heroesAvatarXModifiers.add(1);
        heroesAvatarYModifiers.add(-13);
        heroesAvatarXModifiers.add(5);
        heroesAvatarYModifiers.add(-15);
        heroesAvatarXModifiers.add(5);
        heroesAvatarYModifiers.add(-13);
        heroesAvatarXModifiers.add(0);
        heroesAvatarYModifiers.add(-9);
        heroesAvatarXModifiers.add(0);
        heroesAvatarYModifiers.add(-6);
        //heroesAvatarXModifiers.add(10);heroesAvatarYModifiers.add(-7);
    }

    @Override
    public Parent getPanel() {
        //Yucky hard-conding
        Rectangle map = new Rectangle(HardCodedParameters.defaultWidth - 10, -100 + HardCodedParameters.defaultHeight);
        map.setFill(Color.WHITE);
        map.setStroke(Color.DIMGRAY);
        map.setStrokeWidth(5);
        map.setArcWidth(20);
        map.setArcHeight(20);
        map.setTranslateX(5);
        map.setTranslateY(5);

        Text greets = new Text(-100 + HardCodedParameters.defaultWidth / 2., -40 + HardCodedParameters.defaultHeight, "Round 1");
        greets.setFont(new Font(50));

        int index = heroesAvatarViewportIndex / spriteSlowDownRate;
        heroesAvatar.setViewport(heroesAvatarViewports.get(index));
        heroesAvatar.setScaleX(0.5);
        heroesAvatar.setScaleY(0.5);
        heroesAvatar.setTranslateX(data.getHeroesPosition().x + (-heroesAvatarViewports.get(index).getWidth() / 2. + 0.5 * heroesAvatarXModifiers.get(index)));
        heroesAvatar.setTranslateY(data.getHeroesPosition().y + (-heroesAvatarViewports.get(index).getHeight() / 2. + 0.5 * heroesAvatarYModifiers.get(index)));
        heroesAvatarViewportIndex = (heroesAvatarViewportIndex + 1) % (heroesAvatarViewports.size() * spriteSlowDownRate);

        Group panel = new Group();
        panel.getChildren().addAll(map, greets, heroesAvatar);

        for (PhantomService p : data.getPhantoms()) {
            Circle phantomAvatar = new Circle(10, Color.rgb(255, 156, 156));
            phantomAvatar.setEffect(new Lighting());
            phantomAvatar.setTranslateX(p.getPosition().x);
            phantomAvatar.setTranslateY(p.getPosition().y);
            panel.getChildren().add(phantomAvatar);
        }

        return panel;
    }
}
