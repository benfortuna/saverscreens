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

import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

/**
 * @author Ben Fortuna
 * 
 * Provides a base class with common implementations for panel-based
 * screensavers. (ie. consisting of components)
 */
public abstract class ScreensaverPanel extends AbstractScreensaver {
	
	private JPanel panel;
	
	private boolean containerDisplay;

	/**
	 * Constructor.
	 */
	public ScreensaverPanel(JPanel panel) {
		this.panel = panel;
		this.panel.setOpaque(true);
	}
	
    /**
     * Initialises the panel for display.
     */
    public void initialise() {
    	Component c = getDisplay();
    	
    	/*
        if (!(c instanceof Container)) {
        	throw new IllegalArgumentException("Invalid display component - must be a container!");
    	}
    	*/

		if (c instanceof RootPaneContainer) {
		    ((RootPaneContainer) c).getContentPane().add(panel);
		    
		    containerDisplay = true;
		}
		else if (c instanceof Container) {
		    ((Container) c).add(panel);
		    
		    containerDisplay = true;
		}
		else {
//			Window w = ScreensaverUtils.createScreensaverWindow(this);
//			w.add(panel);
//			panel.setVisible(true);
		    containerDisplay = false;
		}
		
		c.validate();
    }

    /* (non-Javadoc)
	 * @see net.fortuna.idletime.Screensaver#render(java.awt.Graphics)
	 */
	public void render() {
		
//		panel.repaint();
		
		if (!containerDisplay) {
//			Dimension size = getDisplay().getSize();
			
//			BufferedImage bim = (BufferedImage) getDisplay().createImage(size.width, size.height);

//			panel.printAll(bim.createGraphics());
			
//			getDisplay().getGraphics().drawImage(bim, 0, 0, panel);
		}
	}
}
