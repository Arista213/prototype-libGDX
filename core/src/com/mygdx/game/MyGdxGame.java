package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

import static com.mygdx.game.Configuration.*;

public class MyGdxGame extends ApplicationAdapter {
    private static final int CIRCLE_RADIUS = 64;
    private long lastCircleSpawnTime;

    private Texture circleTexture;

    private SpriteBatch batch;

    private Array<Circle> circles;
    private HashMap<Circle, Vector3> circlesDirection;

    @Override
    public void create() {
        batch = new SpriteBatch();
        circleTexture = new Texture("circle.png");

        circles = new Array<>(CIRCLES_MAX_COUNT);
        circlesDirection = new HashMap<>(CIRCLES_MAX_COUNT);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.8f, 0.8f, 0.8f, 1);

        if (circles.size < CIRCLES_MAX_COUNT && TimeUtils.nanoTime() - lastCircleSpawnTime > CIRCLE_RESPAWN_TIME) {
            spawnCircle();
            lastCircleSpawnTime = TimeUtils.nanoTime();
        }

        batch.begin();
        drawCircles();
        moveCircles();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        circleTexture.dispose();
    }

    private void drawCircles() {
        for (Circle circle : circles)
            batch.draw(circleTexture, circle.x, circle.y, circle.radius, circle.radius);
    }

    private void spawnCircle() {
        Circle circle = new Circle();
        circle.radius = CIRCLE_RADIUS;
        circle.x = MathUtils.random(CIRCLE_RADIUS + 1, WIDTH - CIRCLE_RADIUS - 1);
        circle.y = MathUtils.random(CIRCLE_RADIUS + 1, HEIGHT - CIRCLE_RADIUS - 1);

        setCircleRandomDirection(circle);
        circles.add(circle);
    }

    private void moveCircles() {
        for (Map.Entry<Circle, Vector3> entry : circlesDirection.entrySet()) {
            Circle circle = entry.getKey();
            Vector3 direction = entry.getValue();
            circle.x += CIRCLE_SPEED * Gdx.graphics.getDeltaTime() * direction.x;
            circle.y += CIRCLE_SPEED * Gdx.graphics.getDeltaTime() * direction.y;

            if (circle.x < 0) direction.x = Math.abs(direction.x);
            if (circle.x > (WIDTH - CIRCLE_RADIUS)) direction.x = -Math.abs(direction.x);
            if (circle.y < 0) direction.y = Math.abs(direction.y);
            if (circle.y > (HEIGHT - CIRCLE_RADIUS)) direction.y = -Math.abs(direction.y);
        }
    }

    private void setCircleRandomDirection(Circle circle) {
        Vector3 direction = new Vector3(generateDirectionCoordinate(), generateDirectionCoordinate(), 0);
        circlesDirection.put(circle, direction);
    }

    private float generateDirectionCoordinate() {
        float randomRadius = 1000;
        float result = MathUtils.random(-randomRadius, randomRadius) / randomRadius;
        if (result == 0) return generateDirectionCoordinate();
        return result;
    }
}
