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
package org.typhonrt.android.java6.gldemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.typhonrt.commons.java6.opengl.utils.*;

import org.typhonrt.android.java6.gldemo.open.gles30.effects.*;
import org.typhonrt.android.java6.gldemo.open.gles31.invert.*;
import org.typhonrt.android.java6.gldemo.open.gles31.raytrace.basic.ComputeBasicRayTrace;

import org.typhonrt.android.java6.opengl.utils.AndroidGLESUtil;

/**
 * GLDemoActivity - Provides a ListView to select OpenGL demo. Warns user if the device GL version is lower than
 * the required version from the given GL demo selected.
 */
public class GLDemoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
 AdapterView.OnItemLongClickListener
{
   public static final int    s_UI_OPTIONS = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_FULLSCREEN
    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

   private GLHeaderAdapter    adapter;

   private IGLVersion         deviceGLVersion;

   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.list_activity_layout);

      Resources resources = getResources();

      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbar.setLogo(R.drawable.ic_launcher);
      toolbar.setTitle(R.string.app_title);
      toolbar.setContentInsetsAbsolute(0, 0);

      setSupportActionBar(toolbar);

      deviceGLVersion = AndroidGLESUtil.getGLVersion(this);

      adapter = new GLHeaderAdapter(this);

      adapter.addSectionHeaderItem(resources.getString(R.string.header_gles_3_0));

      adapter.addItem(GLSLInvert.class, XeGLES3.GLES3_0,
       "https://github.com/typhonrt/modern-java6-android-gldemos/wiki/gles30demo-glslinvert");

      adapter.addItem(GLSLKuwahara.class, XeGLES3.GLES3_0,
       "https://github.com/typhonrt/modern-java6-android-gldemos/wiki/gles30demo-glslkuwahara");

      adapter.addItem(GLSLKuwaharaFBO.class, XeGLES3.GLES3_0,
       "https://github.com/typhonrt/modern-java6-android-gldemos/wiki/gles30demo-glslkuwaharafbo");

      adapter.addSectionHeaderItem(resources.getString(R.string.header_gles_3_1));

      adapter.addItem(ComputeBasicRayTrace.class, XeGLES3.GLES3_1,
       "https://github.com/typhonrt/modern-java6-android-gldemos/wiki/gles31demo-computebasicraytrace");

      adapter.addItem(ComputeInvert.class, XeGLES3.GLES3_1,
       "https://github.com/typhonrt/modern-java6-android-gldemos/wiki/gles31demo-computeinvert");

      adapter.addItem(ComputeInvertSampler.class, XeGLES3.GLES3_1,
       "https://github.com/typhonrt/modern-java6-android-gldemos/wiki/gles31demo-computeinvertsampler");

      ListView listView = (ListView)findViewById(R.id.main_listview);

      listView.setAdapter(adapter);
      listView.setOnItemClickListener(this);
      listView.setOnItemLongClickListener(this);
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

// OnItemClickedListener Implementation -----------------------------------------------------------------------------

   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id)
   {
      if (GLHeaderAdapter.s_TYPE_ITEM == adapter.getItemViewType(position))
      {
         final Intent intent = new Intent(this, adapter.getItem(position));

         IGLVersion demoGLVersion = adapter.getGLVersion(position);

         if (!deviceGLVersion.lessThan(demoGLVersion))
         {
            startActivity(intent);
         }
         else
         {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener()
            {
               public void onClick(DialogInterface dialog, int id)
               {
                  startActivity(intent);
               }
            });

            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener()
            {
               public void onClick(DialogInterface dialog, int id) {}
            });

            builder.create().show();
         }
      }
   }

// OnItemLongClickListener Implementation ---------------------------------------------------------------------------

   @Override
   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
   {
      if (GLHeaderAdapter.s_TYPE_ITEM == adapter.getItemViewType(position))
      {
         String webLink = adapter.getWebLink(position);

         if (webLink != null)
         {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(webLink));
            startActivity(i);
         }
      }

      return true;
   }
}
