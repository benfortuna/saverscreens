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

import java.util.Properties;

/**
 * @author Ben Fortuna
 */
public class ScreensaverFactory {

    private static ScreensaverFactory instance;

    public static ScreensaverFactory getInstance() {
        if (instance == null) {
            instance = new ScreensaverFactory();
        }

        return instance;
    }

    public Screensaver createScreensaver(String className) {
        return createScreensaver(className, null);
    }

    public Screensaver createScreensaver(String className,
            Properties configuration) {
        try {
            Class screensaverClass = Class.forName(className);

            /*
             * if
             * (screensaverClass.getSuperclass().equals(Screensaver3D.class)) {
             * System.out.println("Disabling direct-draw..");
             *  // this is required to make java3d work correctly in //
             * full-screen exclusive mode..
             * System.setProperty("sun.java2d.noddraw","true"); }
             */

            if (configuration != null) { return (Screensaver) screensaverClass
                    .getConstructor(new Class[] { Properties.class})
                    .newInstance(new Object[] { configuration}); }

            return (Screensaver) screensaverClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
