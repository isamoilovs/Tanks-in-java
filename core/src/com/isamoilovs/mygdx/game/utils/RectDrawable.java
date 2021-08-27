package com.isamoilovs.mygdx.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class RectDrawable implements Drawable{
    private Texture texture;
    Color color;
    float x;
    float y;
    int width;
    int height;
    int variant;

    public RectDrawable(Color color) {
        this.color = color;
    }

    public RectDrawable(Color color, int variant) {
        this.color = color;
        this.variant = variant;
    }

    private void createTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.setColor(color.r, color.g, color.b, color.a);
        if(this.variant == 0) {
            createTexture((int)width, (int)height, color);
            batch.draw(texture, x - 10, y, width + 20, height);
        } else if (this.variant == 1){
            createTexture((int)width, (int)height, color);
            batch.draw(texture, x - 50, y-50, width + 100, height+100);
        }
    }

    @Override
    public float getLeftWidth() {
        return 0;
    }

    @Override
    public void setLeftWidth(float v) {

    }

    @Override
    public float getRightWidth() {
        return 0;
    }

    @Override
    public void setRightWidth(float v) {

    }

    @Override
    public float getTopHeight() {
        return 0;
    }

    @Override
    public void setTopHeight(float v) {

    }

    @Override
    public float getBottomHeight() {
        return 0;
    }

    @Override
    public void setBottomHeight(float v) {

    }

    @Override
    public float getMinWidth() {
        return 0;
    }

    @Override
    public void setMinWidth(float v) {

    }

    @Override
    public float getMinHeight() {
        return 0;
    }

    @Override
    public void setMinHeight(float v) {

    }
}
