/**
 * Copyright 2015 Kai Burjack
 * See: https://github.com/LWJGL/lwjgl3/wiki/2.6.1.-Ray-tracing-with-OpenGL-Compute-Shaders-%28Part-I%29
 *
 * Copyright 2015 Michael Leahy / TyphonRT, Inc.
 * Converted to OpenGL ES 3.1 by Michael Leahy
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
package org.typhonrt.android.java6.gldemo.open.gles31.raytrace.basic;

import android.content.res.Resources;

import org.typhonrt.android.java6.gldemo.open.gles31.raytrace.shared.PinholeCamera;
import org.typhonrt.android.java6.gldemo.shared.BaseDemoActivity;

import org.typhonrt.android.java6.opengl.utils.AndroidGLES31Util;
import org.typhonrt.android.java6.opengl.utils.GLBuffer;
import org.typhonrt.android.java6.opengl.utils.GLBufferUtil;

import org.typhonrt.commons.java6.opengl.utils.XeGLES3;

import org.typhonrt.java6.math.MathUtil;
import org.typhonrt.java6.vecmath.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;


import static android.opengl.GLES31.*;

/**
 * ComputeBasicRayTrace
 */
public class ComputeBasicRayTrace extends BaseDemoActivity
{
   private static final String   s_VERT_SHADER_FILE = "shaders/open/gles30/common/directTexture.vert";
   private static final String   s_FRAG_SHADER_FILE = "shaders/open/gles30/common/directTexture.frag";

   private static final String   s_COMP_SHADER_FILE = "shaders/open/gles31/raytrace/basicraytrace.comp";

   private static final String   s_STR_ATTR_TEX_COORD = "aTexCoord";
   private static final String   s_STR_IN_POSITION = "inPosition";

   private static final Vector3f s_VEC3_POSITION = new Vector3f(3.0f, 2.0f, 10.0f);
   private static final Vector3f s_VEC3_LOOKAT = new Vector3f(0.0f, 0.5f, 0.0f);

   private static final float    s_FIELD_OF_VIEW_Y = 60f;
   private static final float    s_NEAR = 1f;
   private static final float    s_FAR = 2f;

   private int                   surfaceWidth, surfaceHeight;

   private GLBuffer              squareBuffer;

   private int                   textureID;

   private int                   computeProgramID;
   private int                   directProgramID;

   private int                   eyeUniform;
   private int                   ray00Uniform;
   private int                   ray10Uniform;
   private int                   ray01Uniform;
   private int                   ray11Uniform;

   private int                   workGroupSizeX, workGroupSizeY;

   private PinholeCamera         camera = new PinholeCamera();

   private final Vector3f        eyeRay = new Vector3f();

   public ComputeBasicRayTrace()
   {
      super(XeGLES3.GLES3_1);  // Require OpenGL ES 3.1 context
   }

   /**
    * Perform ray trace and draw "framebuffer" compute texture
    */
   @Override
   public void onGLDrawFrame()
   {
      glUseProgram(computeProgramID);

      // Set viewing frustum corner rays in shader
      glUniform3f(eyeUniform, camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
      camera.getEyeRay(-1, -1, eyeRay);
      glUniform3f(ray00Uniform, eyeRay.x, eyeRay.y, eyeRay.z);
      camera.getEyeRay(-1, 1, eyeRay);
      glUniform3f(ray01Uniform, eyeRay.x, eyeRay.y, eyeRay.z);
      camera.getEyeRay(1, -1, eyeRay);
      glUniform3f(ray10Uniform, eyeRay.x, eyeRay.y, eyeRay.z);
      camera.getEyeRay(1, 1, eyeRay);
      glUniform3f(ray11Uniform, eyeRay.x, eyeRay.y, eyeRay.z);

      // Bind level 0 of framebuffer texture as writable image in the shader.
      glBindImageTexture(0, textureID, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);

      // Compute appropriate invocation dimension.
      int worksizeX = MathUtil.nextPow2(surfaceWidth);
      int worksizeY = MathUtil.nextPow2(surfaceHeight);

      // Invoke the compute shader, but only if workGroupSize X/Y is defined.
      if (workGroupSizeX > 0 && workGroupSizeY > 0)
      {
         glDispatchCompute(worksizeX / workGroupSizeX, worksizeY / workGroupSizeY, 1);
      }

      // Reset image binding.
      glBindImageTexture(0, 0, 0, false, 0, GL_READ_WRITE, GL_RGBA32F);

      // GL_COMPUTE_SHADER_BIT is the same as GL_SHADER_IMAGE_ACCESS_BARRIER_BIT
      glMemoryBarrier(GL_COMPUTE_SHADER_BIT);


      // Render compute texture ID
      glUseProgram(directProgramID);

      squareBuffer.bind();

      glVertexAttribPointer(glGetAttribLocation(directProgramID, s_STR_IN_POSITION), 3, GL_FLOAT, false,
       GLBufferUtil.s_QUAD_BUFFER_STRIDE, 0);

      glVertexAttribPointer(glGetAttribLocation(directProgramID, s_STR_ATTR_TEX_COORD), 2, GL_FLOAT, false,
       GLBufferUtil.s_QUAD_BUFFER_STRIDE, GLBufferUtil.s_QUAD_UV_OFFSET);

      glBindTexture(GL_TEXTURE_2D, textureID);
      glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
      squareBuffer.unbind();
   }

   public void onGLSurfaceChanged(int width, int height)
   {
      surfaceWidth = width;
      surfaceHeight = height;

      glViewport(0, 0, surfaceWidth, surfaceHeight);

      textureID = AndroidGLES31Util.createTexture(GL_RGBA32F, surfaceWidth, surfaceHeight);

		// Setup camera
      camera.setFrustumPerspective(s_FIELD_OF_VIEW_Y, (float) width / height, s_NEAR, s_FAR);
      camera.setLookAt(s_VEC3_POSITION, s_VEC3_LOOKAT, Vector3f.s_Y);
   }

   public void onGLSurfaceCreated()
   {
      Resources resources = getResources();

      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);

      squareBuffer = GLBufferUtil.createQuadVertexUVBuffer(1);

      computeProgramID = AndroidGLES31Util.buildProgramFromAssets(resources, s_COMP_SHADER_FILE, GL_COMPUTE_SHADER);
      initComputeProgram();

      directProgramID = AndroidGLES31Util.buildProgramFromAssets(resources, s_VERT_SHADER_FILE, s_FRAG_SHADER_FILE);

      glUseProgram(directProgramID);

      // Set sampler2d in GLSL fragment shader to texture unit 0
      glUniform1i(glGetUniformLocation(directProgramID, "uSourceTex"), 0);
   }

   /**
    * Initialize the compute shader.
    */
   private void initComputeProgram()
   {
      glUseProgram(computeProgramID);

      IntBuffer workGroupSize = ByteBuffer.allocateDirect(MathUtil.s_INTEGER_SIZE_BYTES * 3).order(
       ByteOrder.nativeOrder()).asIntBuffer();

      glGetProgramiv(computeProgramID, GL_COMPUTE_WORK_GROUP_SIZE, workGroupSize);

      workGroupSizeX = workGroupSize.get(0);
      workGroupSizeY = workGroupSize.get(1);

      eyeUniform = glGetUniformLocation(computeProgramID, "eye");
      ray00Uniform = glGetUniformLocation(computeProgramID, "ray00");
      ray10Uniform = glGetUniformLocation(computeProgramID, "ray10");
      ray01Uniform = glGetUniformLocation(computeProgramID, "ray01");
      ray11Uniform = glGetUniformLocation(computeProgramID, "ray11");

      glUseProgram(0);
   }
}
