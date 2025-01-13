package com.UniSim.game.Screens;

import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

/**
 * Credits and acknowledgements screen.
 * Lists:
 * - Third-party libraries used
 * - Asset sources and attributions
 * - Development team members
 * Features scrollable text and return to main menu.
 */
public class CreditsScreen implements Screen {
    // Core components
    private UniSim game;              // Main game instance
    private Stage stage;              // UI stage
    private Skin skin;                // UI styling
    private Music music;              // Background music
    private LandingScreen landingScreen;  // Main menu reference
    private Texture backgroundTexture;    // Menu background

    /**
     * Creates the credits screen with acknowledgements.
     * Sets up UI components and loads content.
     *
     * @param game Main game instance
     * @param landingScreen Screen to return to
     * @param music Background music track
     */
    public CreditsScreen(UniSim game, LandingScreen landingScreen, Music music) {
        this.game = game;
        this.landingScreen = landingScreen;
        this.music = music;
        this.skin = new Skin(Gdx.files.internal("uiskin.json")); 
        Gdx.input.setInputProcessor(stage);
        initialize();
    }

    /**
     * Initializes the UI elements for the Credits screen, including the background,
     * the credits text, and the back button.
     */
    private void initialize() {
        stage = new Stage(new FitViewport(2560, 1440));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("LoadScreenBackground.png");

        // Create a table to organize UI elements
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Back button
        Button backButton = new Button(skin);
        backButton.add(new Label("Back", skin));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                 {
                    game.setScreen(landingScreen);  // Return to LandingScreen
                    Gdx.input.setInputProcessor(landingScreen.getStage());  // Reset input processor for LandingScreen
                }
            }
        });

        // Large text box with instructions
        BitmapFont customFont = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        customFont.getData().setScale(0.9f);

        Label.LabelStyle customLabelStyle = new Label.LabelStyle();
        customLabelStyle.font = customFont;
        customLabelStyle.fontColor = Color.BLACK;

        BitmapFont customFont1 = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        customFont1.getData().setScale(0.14f);


        Label.LabelStyle customLabelStyle1 = new Label.LabelStyle();
        customLabelStyle1.font = customFont1;
        customLabelStyle1.fontColor = Color.BLACK;

        Label creditTitleLabel = new Label("Credits:", customLabelStyle);
        Label creditsLabel = new Label(
            "We used several 3rd-party libraries and assets to enhance functionality in the UniSim game system. Below is a list of these resources\n" +
                "LibGDX\n" +
                "Gradle" +
                "Assets: \n" +
                "Speech Bubble - https://opengameart.org/users/charlexmachina\n" +
                "Map textures - https://opengameart.org/users/isaiah658\n" +
                "Background music  https://opengameart.org/users/mrpoly\n" +
                "Positive event sound - https://opengameart.org/content/achievement\n" +
                "Neutral event sound - https://samplefocus.com/samples/ping-bing\n" +
                "Negative event sound - https://pixabay.com/sound-effects/negative-101682/\n" +
                "Town music - https://opengameart.org/content/town-music\n" +
                "Game icons - https://kenney.nl/assets/game-icons\n" +
                "Ending Music - https://opengameart.org/content/rpg-towntravel-or-credits-song,%20end%20music%20cc-by%203.0\n" +
                "\nGame Developed by: Mark Faizi, Ben Faulkner, Lewis Hanson, Ziyad Rashad Hassan, James Haworth",
            customLabelStyle1);
        creditsLabel.setWrap(true);
        creditsLabel.setAlignment(Align.center);

        // Positioning and adding elements to the stage
        creditTitleLabel.setPosition((stage.getViewport().getWorldWidth() - creditTitleLabel.getWidth()) / 2, 1000);
        creditsLabel.setPosition((stage.getViewport().getWorldWidth() - creditsLabel.getWidth()) / 2, 460);

        backButton.setPosition(10, stage.getViewport().getWorldHeight() - 80);
        backButton.setSize(150, 70);

        stage.addActor(creditTitleLabel);
        stage.addActor(creditsLabel);
        stage.addActor(backButton);


    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);  // Set a background color for the Credits screen
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
        skin.dispose(); // Dispose the skin
    }
}
