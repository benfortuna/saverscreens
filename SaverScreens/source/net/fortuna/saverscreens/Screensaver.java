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

/**
 * @author Ben Fortuna
 * 
 * Provides common method declarations which much be implemented by all
 * screensaver implementations.
 */
public interface Screensaver {

    /**
     * Retrieves the preferred graphics device for this screensaver. This will
     * usually equate to:
     * 
     * <code>GraphicsEnvironment.getLocalGraphicsEnvironment()
     * .getDefaultScreenDevice()</code>
     * @return a graphics device
     */
    GraphicsDevice getPreferredDevice();

    /**
     * Retrives the preferred graphics configuration for this screensaver.
     * 
     * For most screensavers this will equate to:
     * 
     * <code>getPreferredDevice().getDefaultConfiguration()</code>
     * 
     * For Java3D screensavers this will equate to:
     * 
     * <code>SimpleUniverse.getPreferredConfiguration()</code>
     * @return a graphics configuration
     */
    GraphicsConfiguration getPreferredConfiguration();

    /**
     * @return the display name of the screensaver
     */
    String getName();
    
    /**
     * Initialise the screensaver for execution.
     */
    void initialise();
    
    /**
     * Render a frame in the execution of the screensaver.
     */
    void render();
    
    void setDisplay(Component c);
    
    Component getDisplay();
}
