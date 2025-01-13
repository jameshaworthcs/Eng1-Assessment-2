package com.UniSim.game;

import com.badlogic.gdx.Gdx;

/**
 * Global game constants and configuration values.
 * Contains settings for:
 * - Physics and rendering conversions
 * - Character properties and movement
 * - Map and screen dimensions
 * - Camera boundaries and scaling
 * - Game rules and timers
 */
public final class Constants
{
    // Physics and rendering conversion
    /** Pixels per meter for physics/graphics conversion */
    public static final float PPM = 16;
    
    // Character properties
    /** Character width in pixels */
    public static final float CHARACTER_SIZE_X = 14;
    /** Character height in pixels */
    public static final float CHARACTER_SIZE_Y = 18;
    /** Base movement speed in pixels/second */
    public static final float CHARACTER_SPEED = 10;
    
    // Game rules
    /** Game session length in seconds (5 minutes) */
    public static final float TIME_LIMIT = 300;
    
    // Map dimensions
    /** Map width in world units */
    public static final float MAP_SIZE_X = 1600 * PPM;
    /** Map height in world units */
    public static final float MAP_SIZE_Y = 800 * PPM;
    
    // Rendering settings
    /** Global render scale multiplier */
    public static final float SCALE = 4;
    /** Building placement grid size */
    public static final float GRID_SIZE = 1;

    // Screen dimensions
    /** Viewport width in world units */
    public static final float SCREEN_SIZE_X = (Gdx.graphics.getWidth()) / SCALE / PPM;
    /** Viewport height in world units */
    public static final float SCREEN_SIZE_Y = (Gdx.graphics.getHeight()) / SCALE / PPM;
    
    // Camera settings
    /** Camera border width (40% of screen) */
    public static final float BORDER_SIZE_X = SCREEN_SIZE_X * 0.4f;
    /** Camera border height (40% of screen) */
    public static final float BORDER_SIZE_Y = SCREEN_SIZE_Y * 0.4f;
    
    /** Minimum X for camera center */
    public static final float CENTER_MIN_X = BORDER_SIZE_X;
    /** Maximum X for camera center */
    public static final float CENTER_MAX_X = SCREEN_SIZE_X - BORDER_SIZE_X;
    /** Minimum Y for camera center */
    public static final float CENTER_MIN_Y = BORDER_SIZE_Y;
    /** Maximum Y for camera center */
    public static final float CENTER_MAX_Y = SCREEN_SIZE_Y - BORDER_SIZE_Y;
}
