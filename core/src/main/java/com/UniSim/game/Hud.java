package com.UniSim.game;

import com.UniSim.game.Stats.PlayerStats;
import com.UniSim.game.Stats.StatsLabels;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private boolean timeUp;

    Label countdownLabel;
    Label timeLabel;

    private PlayerStats stats;

    private ArrayList<StatsLabels> playerStatLabels;

    public Hud(SpriteBatch sb, Skin skin, World world) {
        setTimer(sb);
        setStats(skin, world);
    }

    private void setStats(Skin skin, World world) {
        stats = new PlayerStats();
        playerStatLabels = new ArrayList<>();

        playerStatLabels.add(new StatsLabels(world, stage, skin, 10,200, "SATISFACTION: " + stats.getSatisfaction()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 180, "CURRENCY: " + stats.getCurrency()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 160, "FATIGUE: " + stats.getFatigue()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 140, "KNOWLEDGE: " + stats.getKnowledge()));
    }

    private void setTimer(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;

        viewport = new FitViewport(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(timeLabel).expandX().pad(10);
        table.row();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public boolean isTimeUp() { return timeUp; }
}
