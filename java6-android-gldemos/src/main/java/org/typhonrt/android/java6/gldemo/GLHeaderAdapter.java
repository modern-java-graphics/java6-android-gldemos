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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.typhonrt.android.java6.opengl.utils.AndroidGLESUtil;

import org.typhonrt.commons.java6.opengl.utils.IGLVersion;

/**
 * GLHeaderAdapter -- Provides a custom adapter that colors items red if the required OpenGL version for a demo
 * does not match the supported version of a given device.
 */
public class GLHeaderAdapter extends BaseAdapter
{
   public static final int             s_TYPE_ITEM = 0;
   public static final int             s_TYPE_SEPARATOR = 1;

   private ArrayList<Class>            data = new ArrayList<Class>();
   private TreeMap<Integer, String>    sectionHeader = new TreeMap<Integer, String>();
   private HashMap<Class, IGLVersion>  glVersionMap = new HashMap<Class, IGLVersion>();
   private HashMap<Class, String>      webLinkMap = new HashMap<Class, String>();

   private IGLVersion                  deviceGLVersion;

   private LayoutInflater              inflater;

   public GLHeaderAdapter(Context context)
   {
      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      deviceGLVersion = AndroidGLESUtil.getGLVersion(context);
   }

   public void addItem(final Class item, final IGLVersion glVersion, final String webLink)
   {
      data.add(item);
      glVersionMap.put(item, glVersion);
      webLinkMap.put(item, webLink);
      notifyDataSetChanged();
   }

   public void addSectionHeaderItem(final String item)
   {
      data.add(String.class);
      sectionHeader.put(data.size() - 1, item);
      notifyDataSetChanged();
   }

   public IGLVersion getGLVersion(int position)
   {
      Class clazz = data.get(position);
      return glVersionMap.get(clazz);
   }

   public String getWebLink(int position)
   {
      Class clazz = data.get(position);
      return webLinkMap.get(clazz);
   }

   @Override
   public int getItemViewType(int position)
   {
      return sectionHeader.containsKey(position) ? s_TYPE_SEPARATOR : s_TYPE_ITEM;
   }

   @Override
   public int getViewTypeCount()
   {
      return 2;
   }

   @Override
   public int getCount()
   {
      return data.size();
   }

   @Override
   public Class getItem(int position)
   {
      return data.get(position);
   }

   @Override
   public long getItemId(int position)
   {
      return position;
   }

   public View getView(int position, View convertView, ViewGroup parent)
   {
      ViewHolder holder;
      int rowType = getItemViewType(position);

      if (convertView == null)
      {
         holder = new ViewHolder();
         switch (rowType)
         {
            case s_TYPE_ITEM:
               convertView = inflater.inflate(R.layout.listview_text, null);
               holder.textView = (TextView) convertView.findViewById(R.id.text);
               break;
            case s_TYPE_SEPARATOR:
               convertView = inflater.inflate(R.layout.listview_section, null);
               holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
               break;
         }
         convertView.setTag(holder);
      }
      else
      {
         holder = (ViewHolder) convertView.getTag();
      }

      String text = null;
      int textColor = Color.WHITE;

      switch (rowType)
      {
         case s_TYPE_ITEM:
            Class clazz = data.get(position);
            text = clazz.getSimpleName();
            IGLVersion glVersion = glVersionMap.get(clazz);
            if (glVersion != null)
            {
               textColor = deviceGLVersion.lessThan(glVersion) ? Color.RED : Color.WHITE;
            }
            break;
         case s_TYPE_SEPARATOR:
            text = sectionHeader.get(position);
            break;
      }

      holder.textView.setText(text);
      holder.textView.setTextColor(textColor);

      return convertView;
   }

   public static class ViewHolder
   {
      public TextView textView;
   }
}