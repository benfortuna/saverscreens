/*
 * $Id$ [14/06/2004]
 *
 * Copyright (c) 2005, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.fortuna.saverscreens;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


/**
 * @author benf
 */
public class ScreensaverUtils {

    private static final int WAKEUP_ON_MOUSE_ACTIVITY = 0;

    private static final int WAKEUP_ON_KEYBOARD_ACTIVITY = 1;

    private static final int WAKEUP_ON_ANY_ACTIVITY = 2;

    private static final int CURSOR_IMAGE_WIDTH = 16;

    private static final int CURSOR_IMAGE_HEIGHT = 16;

    //private static final int DEFAULT_WAKEUP_MODE = WAKEUP_ON_ANY_ACTIVITY;

    //private static final Cursor BLANK_CURSOR =
    // Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16,
    // BufferedImage.TYPE_4BYTE_ABGR),new Point(),"blank_cursor");

    public static boolean wakeup = false;

    public static boolean kioskEnabled = false;

    public static Window createScreensaverWindow(final Screensaver s) {
        return createScreensaverWindow(s, WAKEUP_ON_KEYBOARD_ACTIVITY);
    }

    /**
     * Creates a screensaver-mode window. A screensaver window has the
     * following features:
     *  - a full-screen window - hide the mouse pointer - System.exit() on
     * mouse/keyboard activity
     */
    public static Window createScreensaverWindow(final Screensaver s,
            int wakeupMode) {
        GraphicsDevice device = s.getPreferredDevice();

        if (!device.isFullScreenSupported()) return null;

        GraphicsConfiguration gc = s.getPreferredConfiguration();

        final Frame f = new Frame(gc);
        f.setUndecorated(true);
        //f.setIgnoreRepaint(true);
        //device.setFullScreenWindow(f);

        // double buffering..
        //f.createBufferStrategy(2);

        // hide the mouse cursor..
        //f.setCursor(BLANK_CURSOR);
        f.setCursor(getBlankCursor());

        /*
         * // implement listeners.. f.addMouseMotionListener(new
         * MouseMotionAdapter() { public void mouseMoved(MouseEvent e) { wakeup =
         * true; destroyScreensaverWindow(); //System.exit(0); } });
         * 
         * f.addKeyListener(new KeyAdapter() { public void keyPressed(KeyEvent e) {
         * wakeup = true; destroyScreensaverWindow(); //System.exit(0); } });
         */

        if (wakeupMode == WAKEUP_ON_MOUSE_ACTIVITY
                || wakeupMode == WAKEUP_ON_ANY_ACTIVITY) {
            // implement listeners..
            f.addMouseMotionListener(new MouseMotionAdapter() {

                public void mouseMoved(MouseEvent e) {
                    if (kioskEnabled) {
                        if (f.getCursor() != Cursor.getDefaultCursor()) {
                            f.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                    else {
                        //s.wakeup();
                        destroyScreensaverWindow(s);
                        System.exit(0);
                    }
                }
            });
        }

        if (wakeupMode == WAKEUP_ON_KEYBOARD_ACTIVITY
                || wakeupMode == WAKEUP_ON_ANY_ACTIVITY) {
            f.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
                        kioskEnabled = !kioskEnabled;
                    }
                    else {
                        //s.wakeup();
                        destroyScreensaverWindow(s);
                        System.exit(0);
                    }
                }
            });
        }

        device.setFullScreenWindow(f);
        s.setDisplay(f);

        return f;
    }

    /**
     * Destroys any existing screensaver-mode window.
     */
    public static void destroyScreensaverWindow(Screensaver s) {
        /*
         * GraphicsEnvironment env =
         * GraphicsEnvironment.getLocalGraphicsEnvironment(); GraphicsDevice
         * device = env.getDefaultScreenDevice();
         * device.setFullScreenWindow(null);
         */

        s.getPreferredDevice().setFullScreenWindow(null);
    }

    public static boolean isOption(String option, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (option.equals(args[i])) return true;
        }

        return false;
    }

    /**
     * Initialise and execute the specified screensaver.
     * @param screensaver the screensaver to run
     * @param fullscreen specifies whether to run in full-screen mode
     */
    public static void run(final Screensaver screensaver, final boolean fullscreen) {
		
    	Window window = null;
    	
    	if (fullscreen) {
    		window = createScreensaverWindow(screensaver);
    	}
    	else {
    		JFrame f = new JFrame(screensaver.getName());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(300, 300);
            
            window = f;
    	}
    	
    	window.setVisible(true);
		
    	screensaver.setDisplay(window);
		screensaver.initialise();
		
		ScreensaverRunner runner = new ScreensaverRunner(screensaver);
		runner.start();
    }
    
    /**
     * Creates a blank cursor typically used to hide the on-screen cursor.
     * @return a cursor
     */
    public static Cursor getBlankCursor() {
        return Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(CURSOR_IMAGE_WIDTH, CURSOR_IMAGE_HEIGHT,
                        BufferedImage.TYPE_4BYTE_ABGR), new Point(),
                "blank_cursor");
    }
}
