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
package net.fortuna.saverscreens.bounce;

import java.awt.Color;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import net.fortuna.saverscreens.Screensaver3D;
import net.fortuna.saverscreens.ScreensaverUtils;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * @author benf
 */
public class Bounce extends Screensaver3D {

    private static final Bounds APPLICATION_BOUNDS = new BoundingSphere(
            new Point3d(0, 0, 0), 3);

    public Bounce() {
        SimpleUniverse universe = new SimpleUniverse(getCanvas());
        universe.addBranchGraph(createSceneGraph());

        //universe.getViewingPlatform().setNominalViewingTransform();
        Transform3D viewTransform = new Transform3D();
        viewTransform.setTranslation(new Vector3f(0, 0, 3));
        universe.getViewingPlatform().getViewPlatformTransform().setTransform(
                viewTransform);
    }

    private BranchGroup createSceneGraph() {
        // Create object..
        BranchGroup objectBg = createObject();

        // Create rotational node..
        TransformGroup rotateTg = new TransformGroup();
        rotateTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotateTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotateTg.addChild(objectBg);

        /*
         * Transform3D spinAxis = new Transform3D();
         * spinAxis.setTranslation(new Vector3f(0,0,1));
         */

        Alpha alpha = new Alpha(-1, 6000);
        RotationInterpolator spin = new RotationInterpolator(alpha, rotateTg);
        //spin.setTransformAxis(spinAxis);
        spin.setSchedulingBounds(APPLICATION_BOUNDS);

        // Create rotational node 2..
        TransformGroup rotateTg2 = new TransformGroup();
        rotateTg2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotateTg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotateTg2.addChild(rotateTg);

        // Create translational node..
        TransformGroup translateTg = new TransformGroup();
        translateTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        translateTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        translateTg.addChild(rotateTg2);

        BounceBehaviour bb = new BounceBehaviour(translateTg, rotateTg2, spin);
        bb.setSchedulingBounds(APPLICATION_BOUNDS);

        /////////////////////////
        // Create ambient light..
        AmbientLight al = new AmbientLight(new Color3f(0.1f, 0.1f, 0.1f));
        al.setInfluencingBounds(APPLICATION_BOUNDS);

        /////////////////////////////
        // Create directional light..
        DirectionalLight dl = new DirectionalLight(
                new Color3f(0.7f, 0.7f, 0.7f), new Vector3f(1, -1, -1));
        dl.setInfluencingBounds(APPLICATION_BOUNDS);

        /////////////////////
        // Create root node..
        BranchGroup rootBg = new BranchGroup();
        rootBg.addChild(translateTg);
        rootBg.addChild(spin);
        rootBg.addChild(bb);
        rootBg.addChild(al);
        rootBg.addChild(dl);

        rootBg.compile();

        return rootBg;
    }

    private BranchGroup createObject() {
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(new Color3f(1, 1, 1));

        //Color3f foreground = new Color3f(new
        // Color(Integer.parseInt(getConfig().getProperty("bounce.foreground"))));

        Color3f blue = new Color3f(Color.BLUE);
        Color3f white = new Color3f(Color.WHITE);
        Color3f black = new Color3f(Color.BLACK);

        Material m = new Material(blue, black, blue, white, 80f);

        // temporarily wireframe..
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);

        Appearance a = new Appearance();
        //a.setColoringAttributes(ca);
        a.setMaterial(m);
        a.setPolygonAttributes(pa);

        Sphere ball = new Sphere(0.1f);
        ball.setAppearance(a);

        ColorCube cube = new ColorCube(0.1f);

        BranchGroup bg = new BranchGroup();
        //bg.addChild(ball);
        //bg.addChild(cube);

        // load object..
        try {
            double scaleValue = 0.02;
//            new Double(getConfig().getProperty(
//                    "bounce.object.scale")).doubleValue();

            Transform3D scale = new Transform3D();
            scale.setScale(scaleValue);

            TransformGroup scaleTg = new TransformGroup(scale);

            Scene scene = Loaders.loadScene(getClass().getResource("/models/beethoven.obj"));
//                  getConfig().getProperty(
//                    "bounce.object.url"));

            scaleTg.addChild(scene.getSceneGroup());

            bg.addChild(scaleTg);
        }
        catch (Throwable e) {
            e.printStackTrace();

            // load cube if object load fails..
            bg.addChild(cube);
        }

        return bg;
    }

    /**
     * @see com.bfore.screensaver.Screensaver#showConfig()
     */
    public void showConfig() {
    }

    /**
     * @see com.bfore.screensaver.Screensaver#showInfo()
     */
    public void showInfo() {
    }

    /**
     * @see com.bfore.screensaver.Screensaver#getDisplayName()
     */
    public String getName() {
        return "Bounce";
    }

    public static void main(String[] args) {
        // this is required to make java3d work correctly in
        // full-screen exclusive mode..
//        System.setProperty("sun.java2d.noddraw", "true");

        ScreensaverUtils.run(new Bounce(), false);
    }

}
