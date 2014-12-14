/**
 * Copyright 2015 Michael Leahy / TyphonRT, Inc.
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
package org.typhonrt.android.java6.gldemo.open.gles31.invert;

import android.content.res.Resources;
import android.os.Bundle;

import org.typhonrt.commons.java6.opengl.utils.XeGLES3;

import org.typhonrt.android.java6.gldemo.shared.BaseDemoActivity;

import org.typhonrt.android.java6.opengl.utils.AndroidGLES30Util;
import org.typhonrt.android.java6.opengl.utils.GLBufferUtil;

import org.typhonrt.java6.data.option.model.OptionModel;

import org.typhonrt.android.java6.gldemo.R;


import static android.opengl.GLES31.*;

public class ComputeInvertSampler extends BaseDemoActivity
{
   private static final String   s_VERT_SHADER_FILE = "shaders/open/gles30/common/directTexture.vert";
   private static final String   s_FRAG_SHADER_FILE = "shaders/open/gles30/common/directTexture.frag";

   private static final String   s_COMP_SHADER_FILE = "shaders/open/gles31/color/invertTextureSampler.comp";

   private static final int      s_WORKGROUP_SIZE = 16;

   private int                   directProgramID, computeProgramID;
   private int                   sourceTextureID, resultTextureID;

   private OptionModel           invertEnabled = new OptionModel("Invert", false);

   public ComputeInvertSampler()
   {
      super(XeGLES3.GLES3_1);  // Require OpenGL ES 3.1 context
   }

   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      drawerControl.setOptionModels(new OptionModel[]{invertEnabled});
   }

   public void onGLDrawFrame()
   {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

      if (invertEnabled.getBooleanValue())
      {
         runComputeFilter(sourceTextureID, resultTextureID, 1024, 1024);
         drawTexture(resultTextureID);
      }
      else
      {
         drawTexture(sourceTextureID);
      }
   }

   private void drawTexture(int textureID)
   {
      glUseProgram(directProgramID);

      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, textureID);

      glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 4, 1);
   }

   private void runComputeFilter(int sourceTextureID, int resultTextureID, int width, int height)
   {
      glUseProgram(computeProgramID);

      // Use sampler for source texture; this shows that compute shaders are still GLSL oriented!
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, sourceTextureID);

      glBindImageTexture(1, resultTextureID, 0, false, 0, GL_WRITE_ONLY, GL_RGBA8);

      glDispatchCompute(width / s_WORKGROUP_SIZE, height / s_WORKGROUP_SIZE, 1);

      // GL_COMPUTE_SHADER_BIT is the same as GL_SHADER_IMAGE_ACCESS_BARRIER_BIT
      glMemoryBarrier(GL_COMPUTE_SHADER_BIT);
   }


   public void onGLSurfaceChanged(int width, int height)
   {
      glViewport(0, 0, width, height);

      // Setup a VBO (Vertex Buffer Object)
      // Note: A VBO is created and binded for the rest of the runtime, so we don't hold onto a GLBuffer reference.
      // Quad (vertex + u/v); height / width provides aspect ratio when width is wider than height
      GLBufferUtil.createQuadVertexUVBuffer((float) height / (float) width).bind();

      glVertexAttribPointer(glGetAttribLocation(directProgramID, "inPosition"), 3, GL_FLOAT, false,
       GLBufferUtil.s_QUAD_BUFFER_STRIDE, 0);

      glVertexAttribPointer(glGetAttribLocation(directProgramID, "aTexCoord"), 2, GL_FLOAT, false,
       GLBufferUtil.s_QUAD_BUFFER_STRIDE, GLBufferUtil.s_QUAD_UV_OFFSET);
   }

   public void onGLSurfaceCreated()
   {
      // Sets the color rendered by glClear
      glClearColor(0.2f, 0.0f, 0.2f, 1.0f);  // Set background to dark purple

      Resources resources = getResources();

      // Create shader programs
      directProgramID = AndroidGLES30Util.buildProgramFromAssets(resources, s_VERT_SHADER_FILE, s_FRAG_SHADER_FILE);
      computeProgramID = AndroidGLES30Util.buildProgramFromAssets(resources, s_COMP_SHADER_FILE, GL_COMPUTE_SHADER);

      // Load texture from R.drawable.flower1024 (flip due to GL coordinates)
      sourceTextureID = AndroidGLES30Util.loadTexture(resources, R.drawable.flower1024, true);

      resultTextureID = AndroidGLES30Util.createTexture(GL_RGBA8, 1024, 1024);

      glUseProgram(computeProgramID);

      glUniform2f(glGetUniformLocation(computeProgramID, "imageSize"), 1024, 1024);

      glUseProgram(directProgramID);

      // Bind texture to texture unit 0
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, sourceTextureID);

      // Set sampler2d in GLSL fragment shader to texture unit 0
      glUniform1i(glGetUniformLocation(directProgramID, "uSourceTex"), 0);

      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);
   }
}
