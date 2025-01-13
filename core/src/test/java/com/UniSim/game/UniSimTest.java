package com.UniSim.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

// note to self on how to run tests:
// .\gradlew.bat clean core:test --info

public class UniSimTest {
    private HeadlessApplication app;
    private TestUniSim game;

    // Mock screen for testing
    private static class TestScreen implements Screen {
        @Override public void show() {}
        @Override public void render(float delta) {}
        @Override public void resize(int width, int height) {}
        @Override public void pause() {}
        @Override public void resume() {}
        @Override public void hide() {}
        @Override public void dispose() {}
    }

    // Test implementation of UniSim that doesn't create SpriteBatch
    private static class TestUniSim extends UniSim {
        public boolean createCalled = false;
        public boolean disposeCalled = false;
        private TestScreen testScreen = new TestScreen();

        @Override
        public void create() {
            createCalled = true;
            setScreen(testScreen);
        }

        @Override
        public void dispose() {
            disposeCalled = true;
            if (getScreen() != null) {
                getScreen().dispose();
            }
        }
    }

    @BeforeEach
    public void setUp() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        
        // Mock GL20
        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Mockito.mock(GL20.class);
        
        // Mock Graphics
        Graphics graphics = Mockito.mock(Graphics.class);
        Mockito.when(graphics.getWidth()).thenReturn(800);
        Mockito.when(graphics.getHeight()).thenReturn(600);
        
        // Create test game instance
        game = new TestUniSim();
        app = new HeadlessApplication(game, config);
        
        Gdx.graphics = graphics;
    }

    @AfterEach
    public void tearDown() {
        app.exit();
    }

    @Test
    public void testGameInitialization() {
        assertTrue(game.createCalled, "Game create() should be called");
        assertNotNull(game.getScreen(), "Initial screen should be set");
        assertTrue(game.getScreen() instanceof TestScreen, "Initial screen should be TestScreen");
    }

    @Test
    public void testGameDisposal() {
        game.dispose();
        assertTrue(game.disposeCalled, "Game dispose() should be called");
    }
} 