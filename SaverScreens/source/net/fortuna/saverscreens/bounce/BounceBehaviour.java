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

import java.util.Enumeration;
import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.Behavior;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnActivation;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

/**
 * @author Ben Fortuna
 * 
 * This behaviour provides the ability to make the specified Group "bounce"
 * (ie. change direction/rotation) when collision with other objects are
 * detected.
 */
public class BounceBehaviour extends Behavior {

    private TransformGroup tg;

    private TransformGroup rotateTg;

    private RotationInterpolator ri;

    private Vector3f dv;

    public BounceBehaviour(BranchGroup objectBg, Bounds bounds) {
        // Create rotational node..
        TransformGroup rotateTg = new TransformGroup();
        rotateTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotateTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotateTg.addChild(objectBg);

        Alpha alpha = new Alpha(-1, 6000);
        RotationInterpolator spin = new RotationInterpolator(alpha, rotateTg);
        //spin.setTransformAxis(spinAxis);
        spin.setSchedulingBounds(bounds);

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

        this.tg = translateTg;
        this.rotateTg = rotateTg2;
        this.ri = spin;
    }

    /**
     * Constructor for BounceBehaviour.
     */
    public BounceBehaviour(TransformGroup tg, TransformGroup rtg,
            RotationInterpolator ri) {
        this.tg = tg;
        this.rotateTg = rtg;
        this.ri = ri;
    }

    /**
     * @see javax.media.j3d.Behavior#initialize()
     */
    public void initialize() {
        Random rng = new Random();

        float dx = (float) Math.random() / 100f; //rng.nextInt(1) / 100f;
        float dy = (float) Math.random() / 100f; //rng.nextInt(1) / 100f;
        float dz = (float) Math.random() / 100f; //rng.nextInt(1) / 100f;

        dv = new Vector3f(dx, dy, dz);

        // update spin direction..
        //Quat4f q = new Quat4f(dv.x,dv.y,dv.z,0);
        AxisAngle4f a = new AxisAngle4f(dv, (float) -Math.PI / 2);

        Transform3D spinAxis = new Transform3D();
        //spinAxis.set(q);
        spinAxis.set(a);

        ri.setTransformAxis(spinAxis);

        wakeupOn(new WakeupOnActivation());
    }

    /**
     * @see javax.media.j3d.Behavior#processStimulus(Enumeration)
     */
    public void processStimulus(Enumeration criteria) {
        Transform3D currentTransform = new Transform3D();
        tg.getTransform(currentTransform);

        Vector3f currentTranslate = new Vector3f();
        currentTransform.get(currentTranslate);

        Vector3f currentDv = new Vector3f();
        dv.get(currentDv);

        // update deltas..
        boolean deltaChanged = false;

        if (currentTranslate.x <= -0.8) {
            dv.x = Math.abs(dv.x);
            deltaChanged = true;
        }
        else if (currentTranslate.x >= 0.8) {
            dv.x = Math.abs(dv.x) * -1;
            deltaChanged = true;
        }

        if (currentTranslate.y <= -0.8) {
            dv.y = Math.abs(dv.y);
            deltaChanged = true;
        }
        else if (currentTranslate.y >= 0.8) {
            dv.y = Math.abs(dv.y) * -1;
            deltaChanged = true;
        }

        if (currentTranslate.z <= -0.8) {
            dv.z = Math.abs(dv.z);
            deltaChanged = true;
        }
        else if (currentTranslate.z >= 0.8) {
            dv.z = Math.abs(dv.z) * -1;
            deltaChanged = true;
        }

        if (deltaChanged) {
            //Quat4f q = new Quat4f(dv.x,dv.y,dv.z,0);
            AxisAngle4f a = new AxisAngle4f(dv, (float) -Math.PI / 2);

            Transform3D axis = new Transform3D();
            //axis.set(q);
            axis.set(a);

            /*
             * AxisAngle4f oldA = new AxisAngle4f(currentDv,(float)-Math.PI/2);
             * Transform3D oldAxis = new Transform3D(); oldAxis.set(oldA);
             */
            //currentTransform.mul(rotation);
            ri.setTransformAxis(axis);
            //ri.setAlpha(new Alpha(-1,6000));

            /*
             * if (!ri.getAlpha().isPaused()) {
             * 
             * ri.getAlpha().pause(); }
             */

            Transform3D currentRotation = new Transform3D();
            rotateTg.getTransform(currentRotation);

            Transform3D rotation = new Transform3D();
            ri.getTarget().getTransform(rotation);

            /*
             * System.out.println(axis); System.out.println(currentTransform);
             * System.out.println(rotation);
             * System.out.println(currentRotation); System.out.println("alpha
             * ["+ri.getAlpha().value()+"]");
             * System.out.println("=============================");
             */

            currentRotation.mul(rotation);
            rotateTg.setTransform(currentRotation);
            rotation.setIdentity();
            ri.getTarget().setTransform(rotation);
            ri.getAlpha().setTriggerTime(
                    System.currentTimeMillis() - ri.getAlpha().getStartTime());
            //}
        }

        // update translation..
        Transform3D transform = new Transform3D();
        transform.setTranslation(dv);

        currentTransform.mul(transform);

        /*
         * if (deltaChanged) { System.out.println(currentTransform);
         * System.out.println("============================="); }
         */

        tg.setTransform(currentTransform);

        // reset..
        wakeupOn(new WakeupOnElapsedTime(20));
    }

    /**
     * This method is called whenever a collision is detected.
     */
    protected void onCollision(WakeupOnCollisionEntry criterion) {
        // option 1 - negate delta vector and add some randomness
        // (inspired by selman)
        dv.negate();
        dv.x += Math.random() / 100f;
        dv.y += Math.random() / 100f;
        dv.z += Math.random() / 100f;

        // option 2 - negate single component(s) of delta vector
        // based on hemisphere in which collision ocurred..
        //
        // this could be acheived by detecting point of collision
        // and determining if the (x,y,z) point's polarity is
        // the same as the delta vector. Those components which
        // are should be negated..
    }
}
