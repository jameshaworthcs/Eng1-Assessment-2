package com.UniSim.game;

import com.badlogic.gdx.Gdx;

/**
 * The Constants class stores all the essential constant values used throughout the game.
 * These constants define game settings, physics parameters, screen dimensions, and more.
 * The values are used globally in the game to maintain consistency and allow for easy adjustments.
 */
public final class Constants
{

    public static final float PPM = 16; // pixels per meter
    public static final float CHARACTER_SIZE_X = 14;// Character size in pixels (50x50)
    public static final float CHARACTER_SIZE_Y = 18;
    public static final float CHARACTER_SPEED = 10;// Movement speed in pixels per second
    public static final float TIME_LIMIT = 300; // Game time limit in seconds (5 minutes)
    // Map size (resized to 4400x2000)
    public static final float MAP_SIZE_X = 1600 * PPM;
    public static final float MAP_SIZE_Y = 800 * PPM;
    public static final float SCALE = 4;

    public static final float GRID_SIZE = 1;

    // Screen size and camera border calculations
    public static final float SCREEN_SIZE_X = (Gdx.graphics.getWidth()) / SCALE / PPM;
    public static final float BORDER_SIZE_X = SCREEN_SIZE_X * 0.4f; // 20% border is 200 pixels
    public static final float CENTER_MIN_X = BORDER_SIZE_X;         // 200 pixels from edge
    public static final float CENTER_MAX_X = SCREEN_SIZE_X - BORDER_SIZE_X; // 800 pixels from edge

    public static final float SCREEN_SIZE_Y = (Gdx.graphics.getHeight()) / SCALE / PPM;
    public static final float BORDER_SIZE_Y = SCREEN_SIZE_Y * 0.4f; // 20% border is 200 pixels
    public static final float CENTER_MIN_Y = BORDER_SIZE_Y;         // 200 pixels from edge
    public static final float CENTER_MAX_Y = SCREEN_SIZE_Y - BORDER_SIZE_Y; // 800 pixels from edge
}
