package com.UniSim.game.Sprites;

import com.UniSim.game.Screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.UniSim.game.Constants.PPM;

/**
 * Visual indicator that appears above buildings when the player is nearby.
 * Shows an animated speech bubble to indicate interaction is possible.
 * Uses a simple two-frame animation that loops continuously.
 */
public class SpeechBubble extends Sprite {
    private Animation<TextureRegion> speechBubble;  // Animated bubble texture
    public World world;                            // Physics world reference
    private float stateTime;                       // Animation timing

    /**
     * Creates a new speech bubble at the specified position.
     * Loads and sets up the animation frames from the texture.
     *
     * @param world Game's physics world
     * @param screen Screen containing the bubble texture
     * @param x X position in world coordinates
     * @param y Y position in world coordinates
     */
    public SpeechBubble(World world, GameScreen screen, float x, float y) {
        super(screen.getSpeechBubbleTexture());
        this.world = world;

        // Set up animation frames from sprite sheet
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), 48, 0, 16, 16));  // First frame
        frames.add(new TextureRegion(getTexture(), 64, 0, 16, 16));  // Second frame
        speechBubble = new Animation<TextureRegion>(0.5f, frames);    // Half second per frame

        setBounds(x, y, 16 / PPM, 16 / PPM);
        setRegion(speechBubble.getKeyFrame(stateTime));
    }

    /**
     * Updates the bubble's animation state.
     * Advances animation time and updates the displayed frame.
     *
     * @param delta Time elapsed since last frame
     */
    public void update(float delta) {
        stateTime += delta;
        setRegion(speechBubble.getKeyFrame(stateTime, true));  // Loop animation
    }
}
