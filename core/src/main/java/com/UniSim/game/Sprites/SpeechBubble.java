package com.UniSim.game.Sprites;

import com.UniSim.game.Screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.UniSim.game.Constants.PPM;

public class SpeechBubble extends Sprite {

    private Animation<TextureRegion> speechBubble;
    public World world;
    private float stateTime;


    public SpeechBubble(World world, GameScreen screen, int x, int y) {
        super(screen.getSpeechBubbleTexture());
        this.world = world;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), 48, 0, 16, 16));
        frames.add(new TextureRegion(getTexture(), 64, 0, 16, 16));
        speechBubble = new Animation<TextureRegion>(0.5f, frames);

        setBounds(x, y, 16 / PPM, 16 / PPM);
        setRegion(speechBubble.getKeyFrame(stateTime));
    }

    public void update(float delta) {
        stateTime += delta;  // Increment stateTime by delta time each frame
        setRegion(speechBubble.getKeyFrame(stateTime, true));
    }
}
