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
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.jdesktop.jdic.screensaver.ScreensaverBase;

/**
 * An abstract class which extends on the saverbeans API to allow
 * testing of screensavers using ordinary components (ie. Frames, etc.)
 * @author benfortuna
 */
public abstract class AbstractScreensaver extends ScreensaverBase implements Screensaver {

	private Component display;
	
    /**
     * @see Screensaver#getPreferredDevice()
     */
    public GraphicsDevice getPreferredDevice() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
    }

    /**
     * @see Screensaver#getPreferredConfiguration()
     */
    public GraphicsConfiguration getPreferredConfiguration() {
        return getPreferredDevice().getDefaultConfiguration();
    }

    /**
     * Returns a string representation.
     * @return a string
     */
    public final String toString() {
        return getName();
    }
    
    /* (non-Javadoc)
	 * @see org.jdesktop.jdic.screensaver.ScreensaverBase#init()
	 */
	protected final void init() {
		initialise();
	}
    
    /* (non-Javadoc)
	 * @see org.jdesktop.jdic.screensaver.ScreensaverBase#renderFrame()
	 */
	public final void renderFrame() {
		render();
	}
	
	/**
	 * @return Returns the display.
	 */
	public final Component getDisplay() {
		
		if (display == null && getContext() != null) {
			display = getContext().getComponent();
		}
		return display;
	}
	
	/**
	 * @param display The display to set.
	 */
	public final void setDisplay(Component display) {
		this.display = display;
	}

}
