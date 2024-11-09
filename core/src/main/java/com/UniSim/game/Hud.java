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
    private Skin skin;
    private World world;

    private Integer worldTimer;
    private float timeCount;
    private boolean timeUp;

    Label countdownLabel;
    Label timeLabel;

    public PlayerStats stats;

    private ArrayList<StatsLabels> playerStatLabels;
    private Label messageLabel;

    public Hud(SpriteBatch sb, Skin skin, World world) {
        this.world = world;
        this.skin = skin;
        setTimer(sb);
        setStats(skin, world);
        createMessageLabel(skin);
    }
    public PlayerStats getStats(){
        return stats;
    }
    private void setStats(Skin skin, World world) {
        stats = new PlayerStats();
        playerStatLabels = new ArrayList<>();

        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 220, "BUILDINGS: " + stats.getBuildingCounter()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10,200, "SATISFACTION: " + stats.getSatisfaction()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 180, "CURRENCY: " + String.format("$%.2f", stats.getCurrency())));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 160, "FATIGUE: " + stats.getFatigue()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 140, "KNOWLEDGE: " + stats.getKnowledge()));
    }
    public void updateStats(Skin skin, World world){
        playerStatLabels.get(0).setText("BUILDINGS: " + stats.getBuildingCounter());
        playerStatLabels.get(1).setText("SATISFACTION: " + stats.getSatisfaction());
        playerStatLabels.get(2).setText("CURRENCY: " + String.format("$%.2f", stats.getCurrency()));
        playerStatLabels.get(3).setText("FATIGUE: " + stats.getFatigue());
        playerStatLabels.get(4).setText("KNOWLEDGE: " + stats.getKnowledge());
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

    private void createMessageLabel(Skin skin) {
        messageLabel = new Label("", skin);
        messageLabel.setPosition(10, 20); // Set the position for the message
        messageLabel.setVisible(false); // Initially hidden
        stage.addActor(messageLabel); // Add the label to the stage
    }

    public void update(float dt){
        updateStats(skin, world);
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
    public float getTimeCount(){
        return worldTimer;
    }

    public boolean isTimeUp() { return timeUp; }

    public void sendMessage(String message){
        messageLabel.setText(message);
        messageLabel.setVisible(true); // Show error message
    }

    public void hideMessage(){
        messageLabel.setVisible(false);
    }


}
