/**
 * Copyright 2015 Kai Burjack
 * See: https://github.com/LWJGL/lwjgl3/wiki/2.6.1.-Ray-tracing-with-OpenGL-Compute-Shaders-%28Part-I%29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.typhonrt.android.java6.gldemo.gles31.raytrace.shared;

import org.typhonrt.java6.vecmath.Matrix4f;
import org.typhonrt.java6.vecmath.Vector3f;
import org.typhonrt.java6.vecmath.Vector4f;

/**
 * PinholeCamera
 */
public class PinholeCamera
{
   private float           fn = 0.001f;
   private float           ff = 10.0f;
   private float           fl = -0.05f;
   private float           fr = 0.05f;
   private float           ft = 0.05f;
   private float           fb = -0.05f;

   /**
    * Whether view matrix needs to be recalculated when requested through
    * {@link #getViewMatrix()}.
    */
   private boolean         refreshViewMatrix = true;

   /**
    * Whether projection matrix needs to be recalculated when requested through
    * {@link #getProjectionMatrix()}.
    */
   private boolean         refreshProjectionMatrix = true;

   private final Vector3f  position = new Vector3f(0.0f, 0.0f, 0.0f);

   private final Vector3f  direction = new Vector3f(0.0f, 0.0f, -1.0f);

   private final Vector3f  up = new Vector3f(0.0f, 1.0f, 0.0f);

   private final Vector3f  right = new Vector3f(1.0f, 0.0f, 0.0f);

   private final Matrix4f  projectionMatrix = new Matrix4f();

   private final Matrix4f  viewMatrix = new Matrix4f();

   /**
    * Orthographic projection is inherently different from perspective
    * projection.
    */
   private boolean         orthographic;

   /* Cached objects reused for computations */
   private Vector3f        tmp0 = new Vector3f();
   private Vector3f        tmp1 = new Vector3f();
   private Vector3f        tmp2 = new Vector3f();
   private Vector4f        tmp3 = new Vector4f();
   private float[]         tmpMatArr = new float[16];
   private Matrix4f        tmpMat = new Matrix4f();

   public Vector3f getDirection()
   {
      return direction;
   }

   /**
    * {@inheritDoc}
    */
   public void setDirection(Vector3f direction)
   {
      this.direction.set(direction);
      this.direction.normalize();
      refreshViewMatrix = true;
   }

   /**
    * Compute the world direction vector based on the given X and Y coordinates
    * in normalized-device space.
    *
    * @param x   the X coordinate within [-1..1]
    * @param y   the Y coordinate within [-1..1]
    * @param res will contain the result
    */
   public void getEyeRay(float x, float y, Vector3f res)
   {
      tmpMat.set(getProjectionMatrix());
      tmpMat.mul(getViewMatrix());
      tmpMat.invert();
      tmp3.set(x, y, 0.0f, 1.0f);
      tmpMat.transform(tmp3);
      tmp3.scale(1.0f / tmp3.w);
      tmp0.set(tmp3.x, tmp3.y, tmp3.z);
      res.set(tmp0);
      res.sub(position);
   }

   /**
    * {@inheritDoc}
    */
   public void setLookAt(Vector3f position, Vector3f lookAt, Vector3f up)
   {
      setPosition(position);
      tmp0.set(up);
      tmp0.normalize();
      tmp1.set(lookAt);
      tmp1.sub(position);
      tmp1.normalize();
      setDirection(tmp1);
      /* right = direction x up */
      right.cross(tmp1, tmp0);
      right.normalize();
		/* up = right x direction */
      tmp0.cross(right, tmp1);
      setUp(tmp0);
   }

   private void doRefreshViewMatrix()
   {
      tmp0.set(direction);
		/* right = direction x up */
      tmp1.cross(tmp0, up);
		/* up = right x direction */
      tmp2.cross(tmp1, tmp0);
      final float[] m = tmpMatArr;
      m[0] = tmp1.x;
      m[1] = tmp1.y;
      m[2] = tmp1.z;
      m[3] = -tmp1.x * position.x - tmp1.y * position.y - tmp1.z * position.z;
      m[4] = tmp2.x;
      m[5] = tmp2.y;
      m[6] = tmp2.z;
      m[7] = -tmp2.x * position.x - tmp2.y * position.y - tmp2.z * position.z;
      m[8] = -tmp0.x;
      m[9] = -tmp0.y;
      m[10] = -tmp0.z;
      m[11] = tmp0.x * position.x + tmp0.y * position.y + tmp0.z * position.z;
      m[12] = 0.0f;
      m[13] = 0.0f;
      m[14] = 0.0f;
      m[15] = 1.0f;
      viewMatrix.set(m);
      refreshViewMatrix = false;
   }

   public Vector3f getPosition()
   {
      return position;
   }

   /**
    * {@inheritDoc}
    */
   public void setPosition(Vector3f position)
   {
      if (this.position.equals(position))
      {
         return;
      }
      this.position.set(position);
      refreshViewMatrix = true;
   }

   public Vector3f getUp()
   {
      return up;
   }

   /**
    * {@inheritDoc}
    */
   public void setUp(Vector3f up)
   {
      if (this.up.equals(up))
      {
         return;
      }
      this.up.set(up);
      refreshViewMatrix = true;
   }

   public Vector3f getRight()
   {
      return right;
   }

   /**
    * Set the distance of the near frustum plane.
    *
    * @param frustumNear the distance of the near frustum plane
    */
   protected void setFrustumNear(float frustumNear)
   {
      if (this.fn == frustumNear)
      {
         return;
      }
      this.fn = frustumNear;
      refreshProjectionMatrix = true;
   }

   public float getFrustumFar()
   {
      return ff;
   }

   /**
    * Set the distance of the far frustum plane.
    *
    * @param frustumFar the distance of the far frustum plane
    */
   protected void setFrustumFar(float frustumFar)
   {
      if (this.ff == frustumFar)
      {
         return;
      }
      this.ff = frustumFar;
      refreshProjectionMatrix = true;
   }

   public float getFrustumLeft()
   {
      return fl;
   }

   /**
    * Set the distance of the left frustum plane.
    *
    * @param frustumLeft the distance of the left frustum plane
    */
   protected void setFrustumLeft(float frustumLeft)
   {
      if (this.fl == frustumLeft)
      {
         return;
      }
      this.fl = frustumLeft;
      refreshProjectionMatrix = true;
   }

   public float getFrustumRight()
   {
      return fr;
   }

   /**
    * Set the distance of the right frustum plane.
    *
    * @param frustumRight the distance of the right frustum plane
    */
   protected void setFrustumRight(float frustumRight)
   {
      if (this.fr == frustumRight)
      {
         return;
      }
      this.fr = frustumRight;
      refreshProjectionMatrix = true;
   }

   public float getFrustumTop()
   {
      return ft;
   }

   /**
    * Set the distance of the top frustum plane.
    *
    * @param frustumTop the distance of the top frustum plane
    */
   protected void setFrustumTop(float frustumTop)
   {
      if (this.ft == frustumTop)
      {
         return;
      }
      this.ft = frustumTop;
      refreshProjectionMatrix = true;
   }

   public float getFrustumBottom()
   {
      return fb;
   }

   /**
    * Set the distance of the bottom frustum plane.
    *
    * @param frustumBottom the distance of the bottom frustum plane
    */
   protected void setFrustumBottom(float frustumBottom)
   {
      if (this.fb == frustumBottom)
      {
         return;
      }
      this.fb = frustumBottom;
      refreshProjectionMatrix = true;
   }

   public void setOrthographic(boolean orthographic)
   {
      if (this.orthographic == orthographic)
      {
         return;
      }
      this.orthographic = orthographic;
      refreshProjectionMatrix = true;
   }

   public boolean isOrthographic()
   {
      return orthographic;
   }

   /**
    * {@inheritDoc}
    */
   public Matrix4f getProjectionMatrix()
   {
      if (refreshProjectionMatrix)
      {
         doRefreshProjectionMatrix();
      }
      return projectionMatrix;
   }

   private void doRefreshProjectionMatrix()
   {
      // Reset projection matrix
      if (!orthographic)
      {
         tmpMatArr[0] = 2.0f * fn / (fr - fl);
         tmpMatArr[1] = 0.0f;
         tmpMatArr[2] = (fr + fl) / (fr - fl);
         tmpMatArr[3] = 0.0f;
         tmpMatArr[4] = 0.0f;
         tmpMatArr[5] = 2.0f * fn / (ft - fb);
         tmpMatArr[6] = (ft + fb) / (ft - fb);
         tmpMatArr[7] = 0.0f;
         tmpMatArr[8] = 0.0f;
         tmpMatArr[9] = 0.0f;
         tmpMatArr[10] = -(ff + fn) / (ff - fn);
         tmpMatArr[11] = -2.0f * ff * fn / (ff - fn);
         tmpMatArr[12] = 0.0f;
         tmpMatArr[13] = 0.0f;
         tmpMatArr[14] = -1.0f;
         tmpMatArr[15] = 0.0f;
         projectionMatrix.set(tmpMatArr);
      }
      else
      {
         tmpMatArr[0] = 2.0f / (fr - fl);
         tmpMatArr[1] = 0.0f;
         tmpMatArr[2] = 0.0f;
         tmpMatArr[3] = -(fr + fl) / (fr - fl);
         tmpMatArr[4] = 0.0f;
         tmpMatArr[5] = 2.0f / (ft - fb);
         tmpMatArr[6] = 0.0f;
         tmpMatArr[7] = -(ft + fb) / (ft - fb);
         tmpMatArr[8] = 0.0f;
         tmpMatArr[9] = 0.0f;
         tmpMatArr[10] = -2.0f / (ff - fn);
         tmpMatArr[11] = -(ff + fn) / (ff - fn);
         tmpMatArr[12] = 0.0f;
         tmpMatArr[13] = 0.0f;
         tmpMatArr[14] = 0.0f;
         tmpMatArr[15] = 1.0f;
         projectionMatrix.set(tmpMatArr);
      }
      refreshProjectionMatrix = false;
   }

   /**
    * {@inheritDoc}
    */
   public void setFrustum(float near, float far, float left, float right, float top, float bottom)
   {
      setOrthographic(true);
      setFrustumNear(near);
      setFrustumFar(far);
      setFrustumLeft(left);
      setFrustumRight(right);
      setFrustumTop(top);
      setFrustumBottom(bottom);
   }

   /**
    * {@inheritDoc}
    */
   public void setFrustumPerspective(float fovY, float aspect, float near, float far)
   {
      setFrustumPerspective(fovY, aspect, near, far, 1, 1, 0, 0);
   }

   /**
    * {@inheritDoc}
    */
   public void setFrustumPerspective(float fovY, float aspect, float near, float far, int tilesX, int tilesY,
    int tileX, int tileY)
   {
      if (Float.isNaN(aspect) || Float.isInfinite(aspect))
      {
         throw new IllegalArgumentException("Illegal value for aspect [" + aspect + "]");
      }
      if (near >= far)
      {
         throw new IllegalArgumentException("The passed value for near [" + near + "] must be greater than far ["
          + far + "]");
      }
      if (tileX < 0 || tileX >= tilesX || tileY < 0 || tileY >= tilesY)
      {
         throw new IllegalArgumentException(
          "The tile indices must be at least 0 and at most the total number of tiles minus 1");
      }
      setOrthographic(false);
      final float h = (float) Math.tan(Math.toRadians(fovY) * 0.5f) * near;
      final float w = h * aspect;
      final float left = -w + (float) tileX / tilesX * 2.0f * w;
      final float right = left + (float) 1.0f / tilesX * 2.0f * w;
      final float bottom = -h + (float) tileY / tilesY * 2.0f * h;
      final float top = bottom + (float) 1.0f / tilesY * 2.0f * h;
      setFrustumLeft(left);
      setFrustumRight(right);
      setFrustumBottom(bottom);
      setFrustumTop(top);
      setFrustumNear(near);
      setFrustumFar(far);
   }

   /**
    * {@inheritDoc}
    */
   public Matrix4f getViewMatrix()
   {
      if (refreshViewMatrix)
      {
         doRefreshViewMatrix();
      }
      return viewMatrix;
   }
}
