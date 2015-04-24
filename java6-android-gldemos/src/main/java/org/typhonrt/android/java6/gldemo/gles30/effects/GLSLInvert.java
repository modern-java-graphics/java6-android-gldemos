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
package org.typhonrt.android.java6.gldemo.gles30.effects;

import android.content.res.Resources;
import android.os.Bundle;

import org.typhonrt.android.java6.gldemo.shared.BaseDemoActivity;

import org.typhonrt.android.java6.opengl.utils.AndroidGLES30Util;
import org.typhonrt.android.java6.opengl.utils.GLBufferUtil;

import org.typhonrt.android.java6.data.option.model.OptionModel;

import org.typhonrt.android.java6.gldemo.R;


import static android.opengl.GLES30.*;

public class GLSLInvert extends BaseDemoActivity
{
   private static final String   s_VERT_SHADER_FILE = "shaders/gles30/common/directTexture.vert";
   private static final String   s_FRAG_SHADER_FILE = "shaders/gles30/color/invertTexture.frag";

   private static final String   s_STR_INTENSITY = "intensity";

   private int                   shaderProgram;

   private OptionModel           invertEnabled = new OptionModel("Invert", false);

   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      drawerControl.setOptionModels(new OptionModel[]{invertEnabled});
   }

   public void onGLDrawFrame()
   {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

      if (drawerControl.isOptionModelDirty())
      {
         glUniform1f(glGetUniformLocation(shaderProgram, s_STR_INTENSITY), invertEnabled.getBooleanValue() ? 1f : 0f);
      }

      glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
   }

   public void onGLSurfaceChanged(int width, int height)
   {
      glViewport(0, 0, width, height);

      // Setup a VBO (Vertex Buffer Object)
      // Note: A VBO is created and binded for the rest of the runtime, so we don't hold onto a GLBuffer reference.
      // Quad (vertex + u/v); height / width provides aspect ratio
      GLBufferUtil.createQuadVertexUVBuffer((float) height / (float) width).bind();

      glVertexAttribPointer(glGetAttribLocation(shaderProgram, "inPosition"), 3, GL_FLOAT, false,
       GLBufferUtil.s_QUAD_BUFFER_STRIDE, 0);

      glVertexAttribPointer(glGetAttribLocation(shaderProgram, "aTexCoord"), 2, GL_FLOAT, false,
       GLBufferUtil.s_QUAD_BUFFER_STRIDE, GLBufferUtil.s_QUAD_UV_OFFSET);
   }

   public void onGLSurfaceCreated()
   {
      // Sets the color rendered by glClear
      glClearColor(0.2f, 0.0f, 0.2f, 1.0f);  // Set background to dark purple

      Resources resources = getResources();

      // Create shader program
      shaderProgram = AndroidGLES30Util.buildProgramFromAssets(resources, s_VERT_SHADER_FILE, s_FRAG_SHADER_FILE);

      // Load texture from R.drawable.flower1024 (flip due to GL coordinates)
      int textureID = AndroidGLES30Util.loadTexture(resources, R.drawable.flower1024, GL_RGBA8, true);

      glUseProgram(shaderProgram);

      // Bind texture to texture unit 0
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, textureID);

      // Set sampler2d in GLSL fragment shader to texture unit 0
      glUniform1i(glGetUniformLocation(shaderProgram, "uSourceTex"), 0);

      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);
   }
}
