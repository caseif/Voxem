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
package net.caseif.voxem;

import static org.lwjgl.input.Keyboard.KEY_A;
import static org.lwjgl.input.Keyboard.KEY_B;
import static org.lwjgl.input.Keyboard.KEY_D;
import static org.lwjgl.input.Keyboard.KEY_DOWN;
import static org.lwjgl.input.Keyboard.KEY_F3;
import static org.lwjgl.input.Keyboard.KEY_GRAVE;
import static org.lwjgl.input.Keyboard.KEY_LEFT;
import static org.lwjgl.input.Keyboard.KEY_RIGHT;
import static org.lwjgl.input.Keyboard.KEY_SPACE;
import static org.lwjgl.input.Keyboard.KEY_UP;
import static org.lwjgl.input.Keyboard.KEY_W;
import static org.lwjgl.input.Keyboard.getEventCharacter;
import static org.lwjgl.input.Keyboard.getEventKey;
import static org.lwjgl.input.Keyboard.getEventKeyState;
import static org.lwjgl.input.Keyboard.isKeyDown;
import static org.lwjgl.input.Keyboard.next;

import net.caseif.voxem.event.Event;
import net.caseif.voxem.event.block.BlockBreakEvent;
import net.caseif.voxem.event.block.BlockPlaceEvent;
import net.caseif.voxem.event.input.KeyPressEvent;
import net.caseif.voxem.gui.GuiElement;
import net.caseif.voxem.gui.GuiFactory;
import net.caseif.voxem.gui.InteractiveElement;
import net.caseif.voxem.util.VboUtil;
import net.caseif.voxem.world.Block;
import net.caseif.voxem.world.Location;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;

public class InputManager {

    private static final int LEFT_1 = KEY_A;
    private static final int LEFT_2 = KEY_LEFT;
    private static final int RIGHT_1 = KEY_D;
    private static final int RIGHT_2 = KEY_RIGHT;
    private static final int JUMP_1 = KEY_W;
    private static final int JUMP_2 = KEY_UP;
    private static final int JUMP_3 = KEY_SPACE;
    private static final int CONSOLE = KEY_GRAVE;

    private static boolean f3 = false;
    private static boolean mouse1 = false;
    private static boolean mouse2 = false;
    private static int mouseX = 0;
    private static int mouseY = 0;

    private static long lastAction = 0;
    private static final long ACTION_WAIT = 200;

    public static final ArrayList<Integer> baseKeysToCheck = new ArrayList<>();
    public static ArrayList<Integer> keysToCheck = new ArrayList<>();
    public static final ArrayList<Boolean> keysDownLastFrame = new ArrayList<>();
    public static final ArrayList<Boolean> keysPressed = new ArrayList<>();

    private static int kStage = 0;

    public static void updateKeys(ArrayList<Integer> keys) {
        keysToCheck = keys;
        keysDownLastFrame.clear();
        keysPressed.clear();
        for (int i = 0; i < keysToCheck.size(); i++) {
            keysDownLastFrame.add(false);
            keysPressed.add(false);
        }
    }

    public static void initialize() {
        baseKeysToCheck.add(CONSOLE);
        InputManager.updateKeys(baseKeysToCheck);
    }

    public static void pollInput() {

        while (next()) {
            int key = getEventKey();
            boolean down = getEventKeyState();
            //boolean repeat = isRepeatEvent(); // I might need this at some point...
            char c = getEventCharacter();
            if (down) {
                Event.fireEvent(new KeyPressEvent(key, c));
                if (kStage < 10) {
                    int req = -1;
                    switch (kStage) {
                        case 0:
                            req = KEY_UP;
                            break;
                        case 1:
                            req = KEY_UP;
                            break;
                        case 2:
                            req = KEY_DOWN;
                            break;
                        case 3:
                            req = KEY_DOWN;
                            break;
                        case 4:
                            req = KEY_LEFT;
                            break;
                        case 5:
                            req = KEY_RIGHT;
                            break;
                        case 6:
                            req = KEY_LEFT;
                            break;
                        case 7:
                            req = KEY_RIGHT;
                            break;
                        case 8:
                            req = KEY_B;
                            break;
                        case 9:
                            req = KEY_A;
                            break;
                        default: // wtf happened
                            break;
                    }
                    if (key == req) {
                        kStage += 1;
                        if (kStage == 10) {
                            SoundManager.generateMeme();
                            Main.dank = 0;
                            VboUtil.updateArray();
                            VboUtil.prepareBindArray();
                            VboUtil.bindArray();
                        }
                    } else {
                        kStage = 0;
                    }
                }
            }
        }

        mouse1 = Mouse.isButtonDown(0);
        mouse2 = Mouse.isButtonDown(1);
        mouseX = Mouse.getX();
        mouseY = Mouse.getY();

        for (GuiElement gui : GuiFactory.guis.values()) {
            if (gui.isActive() && mouse1) {
                gui.checkMousePos();
            }
        }

        if (Main.player != null) {
            if ((isKeyDown(LEFT_1) || isKeyDown(LEFT_2)) && (isKeyDown(RIGHT_1) || isKeyDown(RIGHT_2)))
                Main.player.getVelocity().setX(0f);
            else if (isKeyDown(LEFT_1) || isKeyDown(LEFT_2)) {
                Main.player.setFacingDirection(Direction.LEFT);
                Main.player.getVelocity().setX(-Main.player.getSpeed());
            } else if (isKeyDown(RIGHT_1) || isKeyDown(RIGHT_2)) {
                Main.player.setFacingDirection(Direction.RIGHT);
                Main.player.getVelocity().setX(Main.player.getSpeed());
            } else {
                Main.player.getVelocity().setX(0f);
            }
            if (isKeyDown(JUMP_1) || isKeyDown(JUMP_2) || isKeyDown(JUMP_3)) {
                Main.player.setJumping(true);
            } else {
                Main.player.setJumping(false);
            }
        }
        f3 = isKeyDown(KEY_F3);

    }

    public static void manage() {
        if (f3 && System.currentTimeMillis() - lastAction >= ACTION_WAIT) {
            GuiFactory.guis.get("debug").setActive(!GuiFactory.guis.get("debug").isActive());
            lastAction = System.currentTimeMillis();
        }
        if (mouse1) {
            if (System.currentTimeMillis() - lastAction >= ACTION_WAIT) {
                if (Block.selected != null &&
                        Block.selected.getBlock().getType() != Material.AIR &&
                        Block.selected.getBlock().getType() != Material.BEDROCK) {
                    Block b = Main.player.getLevel().getBlock(Math.floor(Block.selected.getX()),
                            Math.floor(Block.selected.getY())).get();
                    Event.fireEvent(new BlockBreakEvent(Block.selected, b));
                }
                lastAction = System.currentTimeMillis();
            }
        } else if (!InteractiveElement.hasMouseReleased) {
            InteractiveElement.hasMouseReleased = true;
        }

        if (mouse2) {
            if (System.currentTimeMillis() - lastAction >= ACTION_WAIT) {
                if (Block.selected != null) {
                    int x = Block.selected.getX() > Main.player.getX() ?
                            (int) Math.floor(Block.selected.getX()) :
                            (int) Math.floor(Block.selected.getX()) + 1;
                    int y = Block.selected.getY() > Main.player.getY() ?
                            (int) Math.floor(Block.selected.getY()) :
                            (int) Math.floor(Block.selected.getY()) + 1;
                    double playerX = Main.player.getX();
                    double playerY = Main.player.getY();
                    double mouseX = (InputManager.mouseX - GraphicsHandler.xOffset) /
                            (float) Block.length;
                    double mouseY = (Display.getHeight() -
                            InputManager.mouseY - GraphicsHandler.yOffset) /
                            (float) Block.length;
                    double xDiff = mouseX - playerX;
                    double yDiff = mouseY - playerY;
                    double m = yDiff / xDiff;
                    double b = playerY - m * playerX;
                    Location l = null;
                    if (m * x + b >= Block.selected.getY() &&
                            m * x + b <= Block.selected.getY() + 1) {
                        if (x == Block.selected.getX())
                            x -= 1;
                        l = new Location(Main.player.getLevel(), x, Block.selected.getY());
                    } else if ((y - b) / m >= Block.selected.getX() &&
                            (y - b) / m <= Block.selected.getX() + 1) {
                        if (y == Block.selected.getY())
                            y -= 1;
                        l = new Location(Main.player.getLevel(), Block.selected.getX(), y);
                    }
                    if (l != null && l.getBlock().getType() == Material.AIR) {
                        if ((int) playerY == y)
                            playerY -= 1;
                        boolean pBlock = false;
                        if (playerY >= 0 && playerY < Main.world.getChunkHeight())
                            if (l.getX() == (float) Math.floor(playerX) &&
                                    l.getY() == (float) Math.floor(playerY))
                                pBlock = true;
                        if (playerY >= -1 && playerY < Main.world.getChunkHeight() - 1)
                            if (l.getX() == (float) Math.floor(playerX) &&
                                    l.getY() == (float) Math.floor(playerY + 1))
                                pBlock = true;
                        if (!pBlock && l.getY() > 0 &&
                                l.getY() < Main.world.getChunkHeight()) {
                            Block block = new Block(Material.WOOD, l);
                            block.addToWorld();
                            Event.fireEvent(new BlockPlaceEvent(l, block));
                        }
                    }
                }
                lastAction = System.currentTimeMillis();
            }
        }
    }

}
