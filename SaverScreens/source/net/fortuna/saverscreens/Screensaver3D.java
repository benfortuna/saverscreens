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

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Canvas3D;
import javax.swing.RootPaneContainer;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * @author Ben Fortuna
 * 
 * Provides a base class with common implementations for three-dimensional
 * screensavers.
 */
public abstract class Screensaver3D extends AbstractScreensaver {

	private Canvas3D canvas;

	public Screensaver3D() {
		this.canvas = new Canvas3D(getPreferredConfiguration());
	}
	
    /**
     * @see com.bfore.screensaver.Screensaver#getPreferredConfiguration()
     */
    public GraphicsConfiguration getPreferredConfiguration() {
        return SimpleUniverse.getPreferredConfiguration();
    }
	
    /**
     * Initialises the canvas for display.
     */
    public void initialise() {
    	Component c = getDisplay();
    	
    	/*
        if (!(c instanceof Container)) {
        	throw new IllegalArgumentException("Invalid display component - must be a container!");
    	}
    	*/

		if (c instanceof RootPaneContainer) {
		    ((RootPaneContainer) c).getContentPane().add(canvas);
		}
		else if (c instanceof Container) {
		    ((Container) c).add(canvas);
		}
		else {
//			Window w = ScreensaverUtils.createScreensaverWindow(this);
//			w.add(panel);
//			panel.setVisible(true);
		}
		
		c.validate();
    }

    /* (non-Javadoc)
	 * @see net.fortuna.idletime.Screensaver#render(java.awt.Graphics)
	 */
	public void render() {
	}
	
	/**
	 * @return Returns the canvas.
	 */
	public Canvas3D getCanvas() {
		return canvas;
	}
}
