package com.UniSim.game.Sprites;

import com.UniSim.game.Screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.UniSim.game.Constants.*;

/**
 * Player character in the game world.
 * Handles movement, animations, and physics interactions.
 * Uses Box2D for physics and sprite animations for visual feedback.
 */
public class Character extends Sprite {
    /** Movement and animation states for the character */
    public enum State {
        RUN_UP,     // Moving upward
        RUN_DOWN,   // Moving downward
        RUN_X,      // Moving left/right
        STAND_UP,   // Standing still facing up
        STAND_DOWN, // Standing still facing down
        STAND_X     // Standing still facing left/right
    }

    // State tracking
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;

    // Animation textures
    private TextureRegion characterStandUp;    // Standing facing up
    private TextureRegion characterStandDown;  // Standing facing down
    private TextureRegion characterStandX;     // Standing facing left/right
    private Animation<TextureRegion> characterRunUp;    // Running up animation
    private Animation<TextureRegion> characterRunDown;  // Running down animation
    private Animation<TextureRegion> characterRunX;     // Running left/right animation

    // Animation state
    private boolean isGoingRight;  // Direction character is facing horizontally
    private float stateTimer;      // Time in current animation state

    /**
     * Creates a new character with animations and physics body.
     * Sets up all animation frames from the sprite sheet.
     *
     * @param world Box2D world for physics
     * @param screen Game screen containing character texture
     */
    public Character(World world, GameScreen screen) {
        super(screen.getCharacterTexture());
        this.world = world;
        currentState = State.STAND_DOWN;
        previousState = State.STAND_DOWN;
        stateTimer = 0.0f;
        isGoingRight = false;

        // Load running animations
        Array<TextureRegion> frames = new Array<TextureRegion>();
        
        // Down animation
        frames.add(new TextureRegion(getTexture(), 17, 4, 14, 18));
        frames.add(new TextureRegion(getTexture(), 33, 4, 14, 18));
        characterRunDown = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        
        // Up animation
        frames.add(new TextureRegion(getTexture(), 17, 28, 14, 18));
        frames.add(new TextureRegion(getTexture(), 33, 28, 14, 18));
        characterRunUp = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        
        // Left/right animation
        frames.add(new TextureRegion(getTexture(), 17, 52, 14, 18));
        frames.add(new TextureRegion(getTexture(), 33, 52, 14, 18));
        characterRunX = new Animation<TextureRegion>(0.1f, frames);

        // Load standing frames
        characterStandDown = new TextureRegion(getTexture(), 1, 4, 14, 18);
        characterStandX = new TextureRegion(getTexture(), 1, 51, 14, 18);
        characterStandUp = new TextureRegion(getTexture(), 1, 28, 14, 18);

        defineCharacter();
        setBounds(0, 0, CHARACTER_SIZE_X / PPM, CHARACTER_SIZE_Y / PPM);
        setRegion(characterStandDown);
    }

    /**
     * Creates the physics body and collision shape for the character.
     * Sets up a rectangular collision box and marks it as the player.
     */
    private void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(800 / PPM, 250 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(CHARACTER_SIZE_X / 2 / PPM, CHARACTER_SIZE_Y / 2 / PPM);

        fdef.shape = shape;
        Fixture characterFixture = b2body.createFixture(fdef);
        characterFixture.setUserData("player");
        shape.dispose();
    }

    /**
     * Updates character position and animation each frame.
     * Syncs sprite position with physics body and updates animation frame.
     *
     * @param delta Time since last frame
     */
    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    /**
     * Gets the current animation frame based on movement state.
     * Handles flipping sprites when changing direction.
     *
     * @param delta Time since last frame
     * @return Current animation frame
     */
    public TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;

        switch (currentState) {
            case RUN_X:
                region = characterRunX.getKeyFrame(stateTimer, true);
                if (isGoingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (!isGoingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                break;
            case RUN_UP:
                region = characterRunUp.getKeyFrame(stateTimer, true);
                break;
            case RUN_DOWN:
                region = characterRunDown.getKeyFrame(stateTimer, true);
                break;
            case STAND_DOWN:
                region = characterStandDown;
                break;
            case STAND_X:
                region = characterStandX;
                if (isGoingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (!isGoingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                break;
            default:
                region = characterStandUp;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    /**
     * Determines character state based on movement velocity.
     * Uses previous state to choose correct standing animation.
     */
    public State getState() {
        if (b2body.getLinearVelocity().y > 0) {
            return State.RUN_UP;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.RUN_DOWN;
        } else if (b2body.getLinearVelocity().x > 0) {
            isGoingRight = true;
            return State.RUN_X;
        } else if (b2body.getLinearVelocity().x < 0) {
            isGoingRight = false;
            return State.RUN_X;
        } else {
            if (previousState == State.RUN_DOWN || previousState == State.STAND_DOWN) {
                return State.STAND_DOWN;
            } else if (previousState == State.RUN_X || previousState == State.STAND_X) {
                return State.STAND_X;
            } else {
                return State.STAND_UP;
            }
        }
    }
}
