package com.UniSim.game.Screens;

import com.UniSim.game.PauseMenu;
import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

import java.awt.*;

/**
 * Tutorial screen that explains game mechanics and controls.
 * Features:
 * - Scrollable instructions with sections
 * - Starting resources and rules
 * - Building types and their effects
 * - Game mechanics and tips
 * Can be accessed from main menu, pause menu, or game screen.
 */
public class HowToPlayScreen implements Screen {
    // Core components
    private UniSim game;              // Main game instance
    private Stage stage;              // UI stage
    private Skin skin;                // UI styling
    private Texture backgroundTexture;  // Menu background
    
    // Navigation references
    private LandingScreen landingScreen;  // Main menu reference
    private PauseMenu pauseMenu;          // Pause menu reference
    private GameScreen gameScreen;        // Game screen reference
    private Music music;                  // Background music

    /**
     * Constructor for HowToPlayScreen when accessed from GameScreen.
     * Initializes the screen with the game instance, game screen, and background music.
     *
     * @param game The game instance.
     * @param gameScreen The GameScreen to return to.
     * @param music Background music for the screen.
     */
    public HowToPlayScreen(UniSim game, GameScreen gameScreen, Music music) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.music = music;
        initialize();
    }

    /**
     * Constructor for HowToPlayScreen when accessed from LandingScreen.
     * Initializes the screen with the game instance, landing screen, and background music.
     *
     * @param game The game instance.
     * @param landingScreen The LandingScreen to return to.
     * @param music Background music for the screen.
     */
    public HowToPlayScreen(UniSim game, LandingScreen landingScreen, Music music) {
        this.game = game;
        this.landingScreen = landingScreen;
        this.music = music;
        initialize();
    }

    /**
     * Constructor for HowToPlayScreen when accessed from PauseMenu.
     * Initializes the screen with the game instance, pause menu, and background music.
     *
     * @param game The game instance.
     * @param pauseMenu The PauseMenu to return to.
     * @param music Background music for the screen.
     */
    public HowToPlayScreen(UniSim game, PauseMenu pauseMenu, Music music) {
        this.game = game;
        this.pauseMenu = pauseMenu;
        this.music = music;
        initialize();
    }

    /**
     * Common initialization method for setting up UI elements, input processor, and background.
     */
    private void initialize() {
        stage = new Stage(new FitViewport(2560, 1440));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("LoadScreenBackground.png");

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Button backButton = new Button(skin);
        backButton.add(new Label("Back", skin));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pauseMenu != null) {
                    pauseMenu.returnToPauseMenu();  // Return to PauseMenu
                    Gdx.input.setInputProcessor(pauseMenu.getStage());  // Reset input processor for PauseMenu
                } else if (landingScreen != null) {
                    game.setScreen(landingScreen);  // Return to LandingScreen
                    Gdx.input.setInputProcessor(landingScreen.getStage());  // Reset input processor for LandingScreen
                } else if (gameScreen != null) {
                    game.setScreen(gameScreen);  // Return to GameScreen
                    Gdx.input.setInputProcessor(gameScreen.getStage());  // Reset input processor for GameScreen
                }
            }
        });

        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        titleFont.getData().setScale(0.2f);

        LabelStyle titleStyle = new LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.BLACK;

        BitmapFont contentFont = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        contentFont.getData().setScale(0.10f);

        LabelStyle contentStyle = new LabelStyle();
        contentStyle.font = contentFont;
        contentStyle.fontColor = Color.BLACK;

        Label titleLabel = new Label("How to Play", titleStyle);
        
        // sections for better organization
        String[] instructions = {
            "Starting Resources:",
            "• You begin with 10,000 pounds\n• An additional 10,000 pounds is given each minute\n• You have five minutes to achieve the highest satisfaction possible\n",
            
            "Building Management:",
            "• Buildings can be bought and placed by pressing escape near the reception\n• Each building has a ten second cooldown between uses\n",
            
            "Activities and Buildings:",
            "• Work: Place work buildings to earn more currency\n" +
            "• Sleep: Use accommodation buildings to reduce fatigue\n" +
            "• Study: Visit academic buildings to increase knowledge\n" +
            "• Eat: Food halls reduce fatigue and increase satisfaction\n" +
            "• Relax: Recreational buildings boost satisfaction\n",
            
            "Game Mechanics:",
            "• Your fatigue level affects movement speed\n• The more fatigued you are, the slower you move\n• Interact with buildings by approaching them"
        };

        Table contentTable = new Table();
        contentTable.defaults().pad(10);
        contentTable.top();

        for (int i = 0; i < instructions.length; i++) {
            Label instructionLabel = new Label(instructions[i], contentStyle);
            instructionLabel.setWrap(true);
            if (i % 2 == 0) {
                // Section headers
                instructionLabel.setStyle(titleStyle);
                contentTable.add(instructionLabel).width(1600).padTop(20);
            } else {
                // Section content
                contentTable.add(instructionLabel).width(1600);
            }
            contentTable.row();
        }

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setScrollingDisabled(true, false);

        // positioning
        titleLabel.setPosition((stage.getViewport().getWorldWidth() - titleLabel.getWidth()) / 2, 1300);
        scrollPane.setBounds(100, 100, stage.getViewport().getWorldWidth() - 200, 1150);

        backButton.setPosition(50, stage.getViewport().getWorldHeight() - 100);
        backButton.setSize(200, 80);

        stage.addActor(titleLabel);
        stage.addActor(scrollPane);
        stage.addActor(backButton);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = new SpriteBatch();

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
