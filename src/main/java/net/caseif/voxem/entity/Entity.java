/*
 * Voxem
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.caseif.voxem.entity;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import net.caseif.voxem.Direction;
import net.caseif.voxem.GraphicsHandler;
import net.caseif.voxem.Main;
import net.caseif.voxem.entity.living.Living;
import net.caseif.voxem.entity.living.Mob;
import net.caseif.voxem.util.ImageUtil;
import net.caseif.voxem.util.Timing;
import net.caseif.voxem.vector.Vector2f;
import net.caseif.voxem.world.Block;
import net.caseif.voxem.world.Level;
import net.caseif.voxem.world.Location;

import com.google.common.base.Optional;

import java.util.HashMap;

public class Entity {

    /**
     * The speed at which entities will fall
     */
    public static final float gravity = 10f;

    /**
     * The terminal downwards velocity of entities
     */
    public static final float terminalVelocity = 40f;

    /**
     * The width of the entity relative to a block's width
     */
    public final float width;

    /**
     * The height of the entity relative to a block's height
     */
    public final float height;

    /**
     * The entity's current velocity.
     */
    public Vector2f velocity = new Vector2f(0f, 0f);

    /**
     * The vertical offset in pixels of entities in relation to the block they are standing on.
     */
    public static final int vertOffset = Block.length / Block.horAngle / 2;

    public static final HashMap<EntityType, Integer> sprites = new HashMap<>();

    protected Location location;
    protected EntityType type;
    protected boolean ground = false;
    protected boolean removed = false;

    public Entity(EntityType type, Location location, float width, float height) {
        this.type = type;
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public synchronized Level getLevel() {
        return location.getLevel();
    }

    public Location getLocation() {
        return location;
    }

    public float getX() {
        return location.getX();
    }

    public float getY() {
        return location.getY();
    }

    public EntityType getType() {
        return type;
    }

    public void setLevel(Level level) {
        location.setLevel(level);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setX(float x) {
        location.setX(x);
    }

    public void setY(float y) {
        location.setY(y);
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void manageMovement() {
        synchronized (this) {
            if (!isOnGround()) {
                if (getVelocity().getY() < terminalVelocity) {
                    float newFallSpeed = getVelocity().getY() + gravity * Timing.delta / Timing.TIME_RESOLUTION * 2.5f;
                    if (newFallSpeed > terminalVelocity)
                        newFallSpeed = terminalVelocity;
                    getVelocity().setY(newFallSpeed);
                }
            }

            float newY = getY() + getVelocity().getY() * (Timing.delta / Timing.TIME_RESOLUTION);

            if (newY > this.getLevel().getWorld().getChunkHeight() && type != EntityType.PLAYER) {
                this.getLevel().removeEntity(this);
                this.removed = true;
                return;
            }

            float pX = getX() >= 0 ? getX() :
                    getX() - 1;

            if (newY >= 0 && newY < Main.world.getChunkHeight()) {
                Optional<Block> b =
                        getLevel().getBlock(pX, (float) Math.floor(newY + vertOffset / (float) Block.length));
                if (getVelocity().getY() > 0
                        && !Block.isAir(b)
                        && b.get().isSolid()) {
                    getVelocity().setY(0);
                }
            }

            Block below = getBlockBelow().orNull();
            //TODO: take into account potentially two blocks
            if (newY - getY() < 1) {
                if (getVelocity().getY() > 0 && below != null && below.isSolid()
                        && (float) below.getY() - getY() < height) {
                    getVelocity().setY(0);
                    setY(below.getY() - height);
                }
            } else {
                for (int y = (int) Math.floor(getY()); y <= newY + height; y++) {
                    if (!Block.isAir(getLevel().getBlock(pX, y))) {
                        getVelocity().setY(0);
                        setY(y - height);
                        break;
                    }
                }
            }
            setY(getY() + getVelocity().getY() * (Timing.delta / Timing.TIME_RESOLUTION));

            if (!isXMovementBlocked()) {
                setX(getX() + getVelocity().getX() * (Timing.delta / Timing.TIME_RESOLUTION));
            } else {
                getVelocity().setX(0);
                if (this instanceof Mob) {
                    ((Mob) this).setPlannedWalkDistance(0);
                    ((Mob) this).setActualWalkDistance(0);
                }
            }
        }
    }

    public boolean isOnGround() {
        Block below = getBlockBelow().orNull();
        if (below != null && below.isSolid()) {
            ground = true;
            return true;
        } else {
            ground = false;
            return false;
        }
    }

    /**
     * Determines whether this entity is marked for removal.
     *
     * @return whether this entity is marked for removal
     */
    public boolean isRemoved() {
        return removed;
    }

    public Optional<Block> getBlockBelow() {
        Optional<Block> below = Optional.absent();
        if (Math.floor(getY() + height) < Main.world.getChunkHeight()) {
            float x = (Math.abs(getX()) % 1 >= width / 2 && getX() > 0) || (Math.abs(getX()) % 1 <= width / 2 &&
                    getX() < 0) ? getX() - width / 4 : getX() + width / 4;
            if (x < 0)
                x -= 1;
            if (getY() >= -height)
                below = getLevel().getBlock(x, (float) Math.floor(getY() + height));
        }
        return below;
    }

    public boolean isXMovementBlocked() {
        float newX = getX() >= 0 ? getX() + (getVelocity().getX() * (Timing.delta / Timing.TIME_RESOLUTION)) :
                getX() - 1 + (getVelocity().getX() * (Timing.delta / Timing.TIME_RESOLUTION));
        newX += (getVelocity().getX() >= 0 ? width : -width) / 2f * 0.9;
        int minY = (int) Math.floor(getY());
        int maxY = (int) Math.ceil(getY() + height - 1);
        for (int y = minY; y <= maxY; y++) {
            Optional<Block> b = getLevel().getBlock(newX, y);
            if (!Block.isAir(b) && b.get().isSolid()) {
                return true;
            }
        }
        return !getLevel().isChunkGenerated(new Location(getLevel(), newX, minY).getChunk());
    }

    public void draw() {
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, sprites.get(type));
        glColor3f(1f, 1f, 1f);
        glTranslatef(getX() * Block.length + GraphicsHandler.xOffset - (width / 2) * Block.length,
                getY() * Block.length + GraphicsHandler.yOffset, 0);
        if (this instanceof Living && ((Living) this).getFacingDirection() == Direction.RIGHT) {
            glTranslatef(Block.length * width, 0f, 0f);
            glScalef(-1f, 1f, 1f);
        }
        glBegin(GL_QUADS);
        int hWidth = (int) (Block.length * width);
        int hHeight = (int) (Block.length * height);
        glTexCoord2f(0f, 0f);
        glVertex2f(0f, vertOffset);
        glTexCoord2f(1f, 0f);
        glVertex2f(hWidth, vertOffset);
        glTexCoord2f(1f, 1f);
        glVertex2f(hWidth, hHeight - vertOffset);
        glTexCoord2f(0f, 1f);
        glVertex2f(0f, hHeight - vertOffset);
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);
        glPopMatrix();
    }

    public static void initialize() {
        for (EntityType et : EntityType.values()) {
            if (et != EntityType.ITEM_DROP) {
                try {
                    String fileName = et.toString().toLowerCase();
                    if (et == EntityType.PLAYER)
                        fileName = "human";
                    Entity.sprites.put(et, ImageUtil.createTextureFromStream(
                            Living.class.getClassLoader().getResourceAsStream(
                                    "textures/entity/" + fileName + ".png"
                            )
                    ));
                } catch (Exception ex) {
                    System.err.println("Exception occurred while preparing texture for " +
                            et.toString().toLowerCase().replace("_", " ") + " sprite");
                    ex.printStackTrace();
                }
            }
        }
    }

}
