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
package org.typhonrt.android.java6.gldemo.shared;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.typhonrt.android.java6.data.option.control.OptionModelDrawerControl;

import org.typhonrt.android.java6.opengl.utils.GLSurfaceView2;

import org.typhonrt.commons.java6.opengl.utils.IGLVersion;
import org.typhonrt.commons.java6.opengl.utils.XeGLES3;

import org.typhonrt.android.java6.gldemo.R;

/**
 * BaseDemoActivity - Only setting the requested OpenGL major version will create the highest major / minor version
 * GL context supported by the given device.
 */
public abstract class BaseDemoActivity extends ActionBarActivity implements GLSurfaceView2.Renderer
{
   public static final int    s_UI_OPTIONS = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_FULLSCREEN
    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

   private static final IGLVersion  s_DEFAULT_GL_VERSION = XeGLES3.GLES3_0;

   protected OptionModelDrawerControl drawerControl;

   protected final IGLVersion       requestedGLVersion;

   protected BaseDemoActivity()
   {
      this(s_DEFAULT_GL_VERSION);
   }

   protected BaseDemoActivity(IGLVersion requestedGLVersion)
   {
      this.requestedGLVersion = requestedGLVersion;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.demo_activity_layout);

      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      drawerControl = new OptionModelDrawerControl(this);

      GLSurfaceView2 surfaceView = (GLSurfaceView2)findViewById(R.id.glsurfaceview);

      surfaceView.setEGLContextGLESVersion(requestedGLVersion);
      surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
      surfaceView.setRenderer(this);
      surfaceView.setRenderMode(GLSurfaceView2.s_RENDERMODE_CONTINUOUSLY);
   }

   @Override
   public void onGLContextCreated()
   {
      GLSurfaceView2 surfaceView = (GLSurfaceView2)findViewById(R.id.glsurfaceview);

      IGLVersion glVersion = surfaceView.getContextGLESVersion();

      final int glMajorVersion = glVersion.getMajorVersion();
      final int glMinorVersion = glVersion.getMinorVersion();

      final int requestedMajorVersion = requestedGLVersion.getMajorVersion();
      final int requestedMinorVersion = requestedGLVersion.getMinorVersion();

      if (glMajorVersion < requestedMajorVersion || (glMajorVersion == requestedMajorVersion &&
       glMinorVersion < requestedMinorVersion))
      {
         runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
               Toast.makeText(BaseDemoActivity.this, "OpenGL ES version less than required!\n"
                +"\nRequested: " +requestedMajorVersion +"." +requestedMinorVersion
                +"\nCreated: " +glMajorVersion +"." +glMinorVersion, Toast.LENGTH_LONG).show();
            }
         });
      }
   }

   /**
    * Handles setting immersive sticky when the window is focused. Necessary when an Activity is minimized and
    * brought back to front.
    *
    * @param hasFocus
    */
   @Override
   public void onWindowFocusChanged(boolean hasFocus)
   {
      super.onWindowFocusChanged(hasFocus);

      if (hasFocus)
      {
         getWindow().getDecorView().setSystemUiVisibility(s_UI_OPTIONS);
      }
   }
}
