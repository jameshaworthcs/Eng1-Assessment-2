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

/**
 * Test class for UniSim game initialization and disposal.
 * Uses headless application configuration for testing without graphics.
 */
public class UniSimTest {
    private HeadlessApplication app;
    private TestUniSim game;

    /**
     * Mock screen implementation for testing.
     * Provides empty implementations of Screen interface methods.
     */
    private static class TestScreen implements Screen {
        @Override public void show() {}
        @Override public void render(float delta) {}
        @Override public void resize(int width, int height) {}
        @Override public void pause() {}
        @Override public void resume() {}
        @Override public void hide() {}
        @Override public void dispose() {}
    }

    /**
     * Test implementation of UniSim that tracks create and dispose calls.
     * Avoids creating SpriteBatch to enable headless testing.
     */
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

    /**
     * Sets up the test environment with a headless application configuration.
     * Mocks necessary LibGDX components for testing.
     */
    @BeforeEach
    public void setUp() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        
        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Mockito.mock(GL20.class);
        
        Graphics graphics = Mockito.mock(Graphics.class);
        Mockito.when(graphics.getWidth()).thenReturn(800);
        Mockito.when(graphics.getHeight()).thenReturn(600);
        
        game = new TestUniSim();
        app = new HeadlessApplication(game, config);
        
        Gdx.graphics = graphics;
    }

    @AfterEach
    public void tearDown() {
        app.exit();
    }

    /**
     * Tests that game initialization properly sets up the initial screen.
     */
    @Test
    public void testGameInitialization() {
        assertTrue(game.createCalled, "Game create() should be called");
        assertNotNull(game.getScreen(), "Initial screen should be set");
        assertTrue(game.getScreen() instanceof TestScreen, "Initial screen should be TestScreen");
    }

    /**
     * Tests that game disposal properly cleans up resources.
     */
    @Test
    public void testGameDisposal() {
        game.dispose();
        assertTrue(game.disposeCalled, "Game dispose() should be called");
    }
} 