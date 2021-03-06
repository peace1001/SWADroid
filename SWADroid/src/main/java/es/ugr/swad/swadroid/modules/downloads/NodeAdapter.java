/*
 *  This file is part of SWADroid.
 *
 *  Copyright (C) 2010 Juan Miguel Boyero Corral <juanmi1982@gmail.com>
 *
 *  SWADroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SWADroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SWADroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ugr.swad.swadroid.modules.downloads;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.ugr.swad.swadroid.R;
import es.ugr.swad.swadroid.gui.FontManager;

/**
 * Adapter to populate browser of files with the information received from SWAD
 *
 * @author Helena Rodriguez Gijon <hrgijon@gmail.com>
 * @author Juan Miguel Boyero Corral <juanmi1982@gmail.com>
 */

public class NodeAdapter extends BaseAdapter {
    private ArrayList<DirectoryItem> list;
    private final Activity mContext;
    private static Typeface iconFont;

    public NodeAdapter(Activity c, ArrayList<DirectoryItem> list) {
        mContext = c;
        this.list = list;

        //Get Font Awesome typeface
        iconFont = FontManager.getTypeface(c, FontManager.FONTAWESOME);
    }

    static class ViewHolder {
        TextView text;
        TextView image;
    }

    @Override
    public int getCount() {
        int size = 0;

        if (list != null) {
            size = list.size();
        }

        return size;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflator = mContext.getLayoutInflater();
            convertView = inflator.inflate(R.layout.grid_item, null);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.icon_text);
            holder.image = (TextView) convertView.findViewById(R.id.icon_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(list.get(position).getName());

        if (list.get(position).isFolder()) {
            holder.image.setText(R.string.fa_folder_open);
        } else {
            holder.image.setText(R.string.fa_file_text);
        }

        //Set Font Awesome typeface
        holder.image.setTypeface(iconFont);

        return convertView;
    }

    public void change(ArrayList<DirectoryItem> newBrowser) {
        list = newBrowser;
        notifyDataSetInvalidated();
    }
}
