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
package net.fortuna.saverscreens.moire;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Random;

import net.fortuna.saverscreens.Screensaver;
import net.fortuna.saverscreens.Screensaver2D;
import net.fortuna.saverscreens.ScreensaverUtils;

/**
 * @author Ben Fortuna
 */
public class Moire extends Screensaver2D {

    private Color background = Color.BLACK;

    private Color foreground = Color.WHITE;

    //Rectangle bounds;
    private Polygon[] moires;

    private int[][] deltas;

    //Window w;
    private Random rng = new Random();

    private boolean backgroundGradient;
    
    /**
     * @see Screensaver#getName()
     */
    public final String getName() {
        return "Moire";
    }

    /**
     * Returns a random point on the screen.
     * 
     * @return a point
     */
    protected final Point getRandomPoint() {
        return new Point(rng.nextInt(1024), rng.nextInt(768));
    }

    protected static Rectangle getMode(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-mode".equals(args[i])) {
                try {

                    int width = Integer.parseInt(args[i + 1].substring(0,
                            args[i + 1].indexOf('x')));
                    int height = Integer.parseInt(args[i + 1]
                            .substring(args[i + 1].indexOf('x') + 1));

                    return new Rectangle(width, height);

                }
                catch (Exception e) {
                }
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.jdesktop.jdic.screensaver.ScreensaverBase#init()
     */
    public void initialise() {

        //int pointCount = DEFAULT_POINTS;

        //try {
        int pointCount = 2; 
            //Integer.parseInt(getConfig().getProperty(
              //  "moire.points"));
        //} catch (Exception e) {}

        Polygon p = new Polygon();

        for (int i = 0; i < pointCount; i++) {
            //p.addPoint(200,200);
            //p.addPoint(400,100);
            //p.addPoint(300,300);
            //p.addPoint(150,300);
            //p.addPoint(100,150);
            Point point = getRandomPoint();
            p.addPoint(point.x, point.y);
        }

        //int duplicateCount = DEFAULT_DUPLICATES;

        //try {
        int duplicateCount = 5;
        //Integer.parseInt(getConfig().getProperty(
          //      "moire.duplicates"));
        //} catch (Exception e) {}

        moires = new Polygon[duplicateCount];

        moires[0] = p;

        //int delta = DEFAULT_DELTA;

        //try {
        int delta = 2;
        //Integer.parseInt(getConfig().getProperty("moire.delta"));
        //} catch (Exception e) {}

        deltas = new int[p.npoints][2];

        for (int i = 0; i < deltas.length; i++) {
            deltas[i][0] = delta;
            deltas[i][1] = delta;
        }

        //foreground = new Color(DEFAULT_FOREGROUND);

        //try {
        foreground = Color.YELLOW;
        //new Color(Integer.parseInt(getConfig().getProperty(
          //      "moire.foreground")));
        //} catch (Exception e) {}

        //background = new Color(DEFAULT_BACKGROUND);

        //try {
        background = Color.BLUE;
        //new Color(Integer.parseInt(getConfig().getProperty(
          //      "moire.background")));
        //} catch (Exception e) {}

        //backgroundGradient = DEFAULT_BACKGROUND_GRADIENT;

        //try {
        backgroundGradient = true;
        //new Boolean(getConfig().getProperty(
          //      "moire.background.gradient")).booleanValue();
        //} catch (Exception e) {}
    }
    
    /* (non-Javadoc)
     * @see org.jdesktop.jdic.screensaver.SimpleScreensaver#paint(java.awt.Graphics)
     */
    public void render(Graphics g) {

        // update positions..
        for (int i = moires.length - 1; i > 0; i--) {
            moires[i] = moires[i - 1];
        }

        Rectangle bounds = getDisplay().getBounds();

        // create new front moire..
        moires[0] = new Polygon();

        for (int i = 0; i < moires[1].npoints; i++) {
            moires[0].addPoint(moires[1].xpoints[i] + deltas[i][0],
                    moires[1].ypoints[i] + deltas[i][1]);

            // update deltas..
            if (moires[0].xpoints[i] < bounds.x) {
                deltas[i][0] = Math.abs(deltas[i][0]);
            }
            else if (moires[0].xpoints[i] > bounds.width) {
                deltas[i][0] = Math.abs(deltas[i][0]) * -1;
            }

            if (moires[0].ypoints[i] < bounds.y) {
                deltas[i][1] = Math.abs(deltas[i][1]);
            }
            else if (moires[0].ypoints[i] > bounds.height) {
                deltas[i][1] = Math.abs(deltas[i][1]) * -1;
            }
        }
        
        //Rectangle bounds = getContext().getComponent().getBounds();
        //Rectangle bounds = moires[0].getBounds();

//            GraphicsUtils.clear(g, bounds, background, backgroundGradient);

        // draw availables moires..
        g.setColor(foreground);

        for (int i = 0; i < moires.length; i++) {
            if (moires[i] != null) {
                g.drawPolygon(moires[i]);
            }
        }

        g.dispose();
    }
    
    public static void main(String[] args) {
		ScreensaverUtils.run(new Moire(), false);
	}
}
