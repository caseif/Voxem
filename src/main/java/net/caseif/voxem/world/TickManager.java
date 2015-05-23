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
package net.caseif.voxem.world;

import net.caseif.voxem.Direction;
import net.caseif.voxem.Main;
import net.caseif.voxem.Material;
import net.caseif.voxem.entity.Entity;
import net.caseif.voxem.entity.EntityType;
import net.caseif.voxem.entity.living.Mob;
import net.caseif.voxem.entity.living.hostile.Ghost;
import net.caseif.voxem.entity.living.passive.Snail;
import net.caseif.voxem.util.Timing;

import java.util.Random;

public class TickManager {

    /**
     * The current tick count
     */
    private static int ticks = 0;

    /**
     * The last timestamp at which a tick elapsed
     */
    private static long lastTick = Timing.getTime();

    /**
     * The number of ticks in a second
     */
    private static final int TICKS_PER_SECOND = 30;

    /**
     * The number of ticks per in-game day
     */
    public static final int TICKS_PER_DAY = 24000;

    /**
     * Chance a single stagnant entity will begin randomly moving ("wandering") in a given tick
     */
    public static final int MOVE_CHANCE = 300;

    /**
     * Maximum blocks an entity may randomly move (wander) at once
     */
    public static final int MAX_MOVE_DISTANCE = 10;

    /**
     * Chance that an entity will spawn in a given tick if the world is at half-mob capacity
     */
    public static final int SPAWN_CHANCE = 600;

    /**
     * The time at which the sky will begin to darken.
     */
    public static final int DUSK_BEGIN = 12000;

    /**
     * The time at which the sky will become its darkest.
     */
    public static final int DUSK_END = 14000;

    /**
     * The time at which the sky will begin to brighten.
     */
    public static final int DAWN_BEGIN = 22000;

    /**
     * The time at which the sky will become its brightest.
     */
    public static final int DAWN_END = 0;

    /**
     * The minimum brightness of the sky.
     */
    public static final float MIN_SKY_BRIGHTNESS = 0.1f;

    /**
     * Retrieves the current tick count of the day.
     *
     * @return The current tick count of the day
     */
    public static int getTicks() {
        return ticks % TICKS_PER_DAY;
    }

    /**
     * Retrieves the total tick count of the world.
     *
     * @return The total tick count of the world
     */
    public static int getTotalTicks() {
        return ticks;
    }

    /**
     * Sets the current tick count of the game.
     */
    public static void setTicks(int ticks) {
        TickManager.ticks = ticks;
    }

    /**
     * Generates RTEs (Random Tick Events) and basically controls every non-player action that happens in the world
     */
    public static void handleTick() {
        if (Main.player.getLevel().getWorld().isTicking() && Main.player.getLevel().chunks.size() > 0) {
            Random r = new Random();
            if (Main.player.getLevel().getMobCount() < Mob.MOB_CAPACITY) { // check that world isn't full
                int actualChance = (int) ((float) Main.player.getLevel().getMobCount()
                        / (float) Mob.MOB_CAPACITY * 2f * (float) SPAWN_CHANCE) + 1;
                if (r.nextInt(actualChance) == 0) {
                    EntityType type = Mob.mobTypes.get(r.nextInt(Mob.mobTypes.size()));
                    Chunk c = Main.player.getLevel().chunks.values().toArray(
                            new Chunk[Main.player.getLevel().chunks.size()]
                    )[r.nextInt(Main.player.getLevel().chunks.size())];
                    double x = Double.POSITIVE_INFINITY;
                    double y = Double.POSITIVE_INFINITY;
                    boolean second = false;
                    while (x == Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY) {
                        for (int xx = 0; xx < Main.world.getChunkLength(); xx++) {
                            for (int yy = 0; yy < Main.world.getChunkHeight(); yy++) {
                                Block b = c.getBlock(xx, yy);
                                if (b != null && b.getLightLevel() <= Mob.getMaximumLightLevel(type) &&
                                        b.getType() == Material.AIR &&
                                        (yy > 0 && (c.getBlock(xx, yy - 1) == null ||
                                                c.getBlock(xx, yy - 1).getType() == Material.AIR)) || yy == 0 &&
                                        (yy < Main.world.getChunkHeight() - 1 &&
                                                (c.getBlock(xx, yy + 1) != null &&
                                                        c.getBlock(xx, yy + 1).getType() != Material.AIR)) ||
                                        yy == Main.world.getChunkHeight() - 1) {
                                    if (r.nextInt(200) == 0) {
                                        x = xx;
                                        y = yy;
                                    }
                                }
                                if (y != Double.POSITIVE_INFINITY)
                                    break;
                            }
                            if (x != Double.POSITIVE_INFINITY)
                                break;
                        }
                        if (second)
                            break; // give up because it'll probably get stuck in a loop
                        else
                            second = true;
                    }
                    switch (type) {
                        case GHOST:
                            Main.player.getLevel().addEntity(new Ghost(new Location(
                                    Main.player.getLevel(),
                                    Chunk.getWorldXFromChunkIndex(c.getIndex(), (int) x),
                                    (float) y
                            )));
                            break;
                        case SNAIL:
                            Main.player.getLevel().addEntity(new Snail(new Location(
                                    Main.player.getLevel(),
                                    Chunk.getWorldXFromChunkIndex(c.getIndex(), (int) x),
                                    (float) y
                            )));
                            break;
                        default:
                            break;
                    }
                }
            }
            for (Entity e : Main.player.getLevel().getEntities()) {
                synchronized (e) {
                    if (e instanceof Mob) { // make sure it's not just an item drop or something
                        Mob m = (Mob) e;
                        if (m.getPlannedWalkDistance() == 0) { // check if entity is already moving
                            if (r.nextInt(MOVE_CHANCE) == 0) { // decide whether entity should move
                                float distance = r.nextInt(MAX_MOVE_DISTANCE) + 1;
                                if (r.nextInt(2) == 0) // move left if 0, else move right
                                    distance *= -1;
                                m.setPlannedWalkDistance(distance); // update
                                // start movement
                                if (distance > 0) {
                                    m.setFacingDirection(Direction.RIGHT);
                                    m.getVelocity().setX(m.getSpeed());
                                } else {
                                    m.setFacingDirection(Direction.LEFT);
                                    m.getVelocity().setX(-m.getSpeed());
                                }
                                m.setLastX(m.getX());
                            }
                        } else {
                            // check if entity should still be moving
                            if (Math.abs(m.getActualWalkDistance()) >= Math.abs(m.getPlannedWalkDistance()) ||
                                    m.getVelocity().getX() == 0) {
                                m.setPlannedWalkDistance(0); // reset
                                m.setActualWalkDistance(0); // reset
                                m.getVelocity().setX(0); // stop the entity
                            } else {
                                m.setActualWalkDistance(m.getActualWalkDistance() + Math.abs(m.getX() - m.getLastX())); // update
                                m.setLastX(m.getX());
                            }
                        }
                    }
                }
            }
            ticks += 1;
            lastTick = Timing.getTime();
        }
    }

    /**
     * Checks whether ticks have elapsed since the last iteration, and if so, handles them
     *
     * @return The number of ticks which have elapsed since the last check
     */
    public static int checkForTick() {
        int elapsed = (int) ((Timing.getTime() - lastTick) / (Timing.TIME_RESOLUTION / 1000)) /
                (1000 / TICKS_PER_SECOND); // elapsed ticks since last tick
        for (int i = 0; i < elapsed; i++)
            handleTick(); // handle each tick separately
        return elapsed;
    }

}
