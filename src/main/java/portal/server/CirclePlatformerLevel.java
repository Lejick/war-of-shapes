/*
 * Copyright (c) 2010-2016 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *     and the following disclaimer in the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or
 *     promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package portal.server;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Step;
import org.dyn4j.dynamics.StepAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.dynamics.contact.PersistedContactPoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple scene of a circle that is controlled by the left and
 * right arrow keys that is moved by applying torques and forces.
 * <p>
 * Also illustrated here is how to track whether the body is in
 * contact with the "ground."
 * <p>
 * Always keep in mind that this is just an example, production
 * code should be more robust and better organized.
 *
 * @author William Bittle
 * @version 3.2.0
 * @since 3.2.5
 */
public class CirclePlatformerLevel {

    protected final World world = new World();

    /**
     * Default constructor for the window
     */
    public CirclePlatformerLevel() {

    }
    private Body wheel;
    public final AtomicBoolean leftPressed = new AtomicBoolean(false);
    public final AtomicBoolean rightPressed = new AtomicBoolean(false);
    public final AtomicBoolean jumpPressed = new AtomicBoolean(false);
    private List<Body> obstaclesList = new ArrayList<>();
    private float height, width;

    /**
     * Creates game objects and adds them to the world.
     */
    protected void initializeWorld(float height, float width) {
        // the floor
        this.height = height;
        this.width = width;
        initObstacles();
        initPlayBody();
        this.world.addBody(wheel);
        for (Body body : obstaclesList) {
            this.world.addBody(body);
        }
    }

    private void initPlayBody() {
        wheel = new Body();
        wheel.addFixture(Geometry.createCircle(width / 40), 1.0, 10.0, 0.5);
        wheel.setMass(MassType.NORMAL);
    }

    private void initObstacles() {
        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(width, width / 100));
        floor.setMass(MassType.INFINITE);
        floor.translate(0, 0);

        Body left = new Body();
        left.addFixture(Geometry.createRectangle(height / 100, height));
        left.setMass(MassType.INFINITE);
        left.translate(-width / 2, height / 2);

        Body right = new Body();
        right.addFixture(Geometry.createRectangle(height / 100, height));
        right.setMass(MassType.INFINITE);
        right.translate(width / 2, height / 2);

        Body roof = new Body();
        roof.addFixture(Geometry.createRectangle(width, width / 100));
        roof.setMass(MassType.INFINITE);
        roof.translate(0, height);

        obstaclesList.add(left);
        obstaclesList.add(right);
        obstaclesList.add(floor);
        obstaclesList.add(roof);
    }

    protected void update(double elapsedTime) {
        // apply a torque based on key input
        if (this.leftPressed.get()) {
            wheel.applyTorque(Math.PI / 2);
        }
        if (this.rightPressed.get()) {
            wheel.applyTorque(-Math.PI / 2);
        }
        if (this.jumpPressed.get()) {
            Vector2 jumpVector = wheel.getForce();
            jumpVector.y = 1;
            wheel.applyImpulse(jumpVector);
        }
        updateWorld(elapsedTime);
    }

    protected void updateWorld(double elapsedTime) {
        this.world.update(elapsedTime);
    }

    public Body getWheel() {
        return wheel;
    }

    public List<Body> getObstaclesList() {
        return obstaclesList;
    }

}
