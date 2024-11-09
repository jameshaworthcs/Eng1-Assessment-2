package com.UniSim.game.Screens;

import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class EndScreen implements Screen {

    private static final float FONT_SCALE = 0.8f;
    private static final float TITLE_FONT_SCALE = 1f;

    private final UniSim game;
    private final AssetManager manager;

    private Stage stage;
    private Texture backgroundTexture;
    private Music music;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont titleFont;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleLabelStyle;

    public EndScreen(UniSim game, Music music) {
        this.game = game;
        this.manager = new AssetManager();

        initializeMusic(music);
        initializeStage();
        initializeUI();
    }

    private void initializeMusic(Music music) {
        this.music = music;
        manager.load("music/Chippytoon.mp3", Music.class);
        manager.finishLoading();

        this.music = manager.get("music/Chippytoon.mp3", Music.class);
        this.music.setLooping(true);
        this.music.setVolume(music.getVolume());
        this.music.play();
    }

    private void initializeStage() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = new Texture("LoadScreenBackground.png");
    }

    private void initializeUI() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        font = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        font.getData().setScale(FONT_SCALE);
        labelStyle = new Label.LabelStyle(font, null);

        titleFont = new BitmapFont(Gdx.files.internal("titleFont.fnt"));
        titleFont.getData().setScale(TITLE_FONT_SCALE);
        titleLabelStyle = new Label.LabelStyle(titleFont, null);

        // Adjust default skin font size
        skin.getFont("default-font").getData().setScale(2f);
    }

    @Override
    public void show() {
        // Show logic here, if needed
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Pause logic here, if needed
    }

    @Override
    public void resume() {
        // Resume logic here, if needed
    }

    @Override
    public void hide() {
        // Hide logic here, if needed
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        music.dispose();
        font.dispose();
        titleFont.dispose();
        skin.dispose();
        manager.dispose();
    }
}

