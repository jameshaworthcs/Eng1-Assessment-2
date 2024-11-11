package com.UniSim.game.Sprites;

import com.UniSim.game.Screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.UniSim.game.Constants.PPM;

/**
 * The SpeechBubble class represents a speech bubble in the game.
 * It is used to show you are close enough to a building to use it.
 * The speech bubble animation is managed by the state time, and the texture frames are set accordingly.
 */
public class SpeechBubble extends Sprite {

    private Animation<TextureRegion> speechBubble;
    public World world;
    private float stateTime;

    /**
     * Constructs a new SpeechBubble at the given position.
     * The speech bubble will display an animation based on the provided texture.
     *
     * @param world The Box2D world the speech bubble will exist in
     * @param screen The GameScreen where the speech bubble will be rendered
     * @param x The x-coordinate to position the speech bubble
     * @param y The y-coordinate to position the speech bubble
     */
    public SpeechBubble(World world, GameScreen screen, float x, float y) {
        super(screen.getSpeechBubbleTexture());
        this.world = world;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), 48, 0, 16, 16));
        frames.add(new TextureRegion(getTexture(), 64, 0, 16, 16));
        speechBubble = new Animation<TextureRegion>(0.5f, frames);

        setBounds(x, y, 16 / PPM, 16 / PPM);
        setRegion(speechBubble.getKeyFrame(stateTime));
    }

    /**
     * Updates the speech bubble's animation by advancing the state time.
     * This method should be called every frame to update the speech bubble.
     *
     * @param delta The time passed since the last frame
     */
    public void update(float delta) {
        stateTime += delta;  // Increment stateTime by delta time each frame
        setRegion(speechBubble.getKeyFrame(stateTime, true));
    }
}
