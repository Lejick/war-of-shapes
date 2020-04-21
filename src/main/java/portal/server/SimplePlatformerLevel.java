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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
 * @author William Bittle
 * @since 3.2.5
 * @version 3.2.0
 */
public class SimplePlatformerLevel {
	/** The serial version id */
	private static final long serialVersionUID = -313391186714427055L;

	protected final World world = new World();
	/**
	 * Default constructor for the window
	 */
	public SimplePlatformerLevel() {

	}
	
	private Body wheel;
	
	public final AtomicBoolean leftPressed = new AtomicBoolean(false);
	public final AtomicBoolean rightPressed = new AtomicBoolean(false);
	private final AtomicBoolean isOnGround = new AtomicBoolean(false);
	private static final Object FLOOR_BODY = new Object();
	private Body floor;
	private Body right;
	private Body left;
	/**
	 * Creates game objects and adds them to the world.
	 */
	protected void initializeWorld(float height, float width) {
		// the floor
		floor = new Body();
		floor.addFixture(Geometry.createRectangle(width, width/100));
		floor.setMass(MassType.INFINITE);
		floor.translate(0, 0);
		floor.setUserData(FLOOR_BODY);
		this.world.addBody(floor);

		// some bounding shapes
		right = new Body();
		right.addFixture(Geometry.createRectangle(height/100,  height));
		right.setMass(MassType.INFINITE);
		right.translate(width / 2, 7);
		this.world.addBody(right);

		left = new Body();
		left.addFixture(Geometry.createRectangle(height / 100, height));
		left.setMass(MassType.INFINITE);
		left.translate(-width / 2, 7);
		this.world.addBody(left);

		// the wheel
		wheel = new Body();
		// NOTE: lots of friction to simulate a sticky tire
		wheel.addFixture(Geometry.createCircle(width / 40), 1.0, 20.0, 0.1);
		wheel.setMass(MassType.NORMAL);
		this.world.addBody(wheel);

		this.world.addListener(new StepAdapter() {
			@Override
			public void begin(Step step, World world) {
				// at the beginning of each world step, check if the body is in
				// contact with any of the floor bodies
				boolean isGround = false;
				List<Body> bodies =  wheel.getInContactBodies(false);
				for (int i = 0; i < bodies.size(); i++) {
					if (bodies.get(i).getUserData() == FLOOR_BODY) {
						isGround = true;
						break;
					}
				}
				
				if (!isGround) {
					// if not, then set the flag, and update the color
					isOnGround.set(false);					
				}
			}
		});
		
		// then, when a contact is created between two bodies, check if the bodies
		// are floor and wheel, if so, then set the color and flag
		this.world.addListener(new ContactAdapter() {
			private boolean isContactWithFloor(ContactPoint point) {
				if ((point.getBody1() == wheel || point.getBody2() == wheel) &&
					(point.getBody1().getUserData() == FLOOR_BODY || point.getBody2().getUserData() == FLOOR_BODY)) {
					return true;
				}
				return false;
			}
			
			@Override
			public boolean persist(PersistedContactPoint point) {
				if (isContactWithFloor(point)) {
					isOnGround.set(true);
				}
				return super.persist(point);
			}
			
			@Override
			public boolean begin(ContactPoint point) {
				if (isContactWithFloor(point)) {
					isOnGround.set(true);
				}
				return super.begin(point);
			}
		});
	}
	
	protected void update(double elapsedTime) {
		// apply a torque based on key input
		if (this.leftPressed.get()) {
			wheel.applyTorque(Math.PI / 2);
		}
		if (this.rightPressed.get()) {
			wheel.applyTorque(-Math.PI / 2);
		}
		updateWorld(elapsedTime);
	}

	protected void updateWorld(double elapsedTime) {
		// update the world with the elapsed time
		this.world.update(elapsedTime);
	}

	public Body getWheel() {
		return wheel;
	}

	public Body getFloor() {
		return floor;
	}

	public Body getRight() {
		return right;
	}

	public Body getLeft() {
		return left;
	}
}
