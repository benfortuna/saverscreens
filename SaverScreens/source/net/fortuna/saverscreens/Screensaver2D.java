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

import java.awt.Graphics;
import java.awt.Window;
import java.awt.image.BufferStrategy;

/**
 * @author Ben Fortuna
 * 
 * Provides a base class with common implementations for two-dimensional
 * screensavers. A two-dimensional screensaver typically paints directly onto
 * the display component's graphics.
 */
public abstract class Screensaver2D extends AbstractScreensaver implements Screensaver {
	
	/* (non-Javadoc)
	 * @see net.fortuna.idletime.Screensaver#render()
	 */
	public void render() {
	    // use acceleration for windows..
	    if (getDisplay() instanceof Window) {
	    	BufferStrategy bufferStrategy = ((Window) getDisplay())
	                .getBufferStrategy();
	
	        Graphics g = bufferStrategy.getDrawGraphics();
	
	        if (!bufferStrategy.contentsLost()) {
	    		render(g);
	            bufferStrategy.show();
	            g.dispose();
	        }
	    }
	    else {
			render(getDisplay().getGraphics());
	    }
	}
	
	public abstract void render(Graphics g);
}
