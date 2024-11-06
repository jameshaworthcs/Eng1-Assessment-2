package com.UniSim.game;

import com.badlogic.gdx.Gdx;

public final class Constants
{

    public static final float PPM = 2; // pixels per meter
    public static final float CHARACTER_SIZE_X = 14;// Character size in pixels (50x50)
    public static final float CHARACTER_SIZE_Y = 18;
    public static final float CHARACTER_SPEED = 10000000;// Movement speed in pixels per second
    // Map size (resized to 4400x2000)
    public static final float MAP_SIZE_X = 1600 * PPM;
    public static final float MAP_SIZE_Y = 800 * PPM;
    public static final float SCALE = 4;

    public static final float GRID_SIZE = 8;

    // Screen size and camera border calculations
    public static final float SCREEN_SIZE_X = (Gdx.graphics.getWidth() - 100) / SCALE / PPM;
    public static final float BORDER_SIZE_X = SCREEN_SIZE_X * 0.2f; // 20% border is 200 pixels
    public static final float CENTER_MIN_X = BORDER_SIZE_X;         // 200 pixels from edge
    public static final float CENTER_MAX_X = SCREEN_SIZE_X - BORDER_SIZE_X; // 800 pixels from edge

    public static final float SCREEN_SIZE_Y = (Gdx.graphics.getHeight() - 100) / SCALE / PPM;
    public static final float BORDER_SIZE_Y = SCREEN_SIZE_Y * 0.3f; // 20% border is 200 pixels
    public static final float CENTER_MIN_Y = BORDER_SIZE_Y;         // 200 pixels from edge
    public static final float CENTER_MAX_Y = SCREEN_SIZE_Y - BORDER_SIZE_Y; // 800 pixels from edge
}
