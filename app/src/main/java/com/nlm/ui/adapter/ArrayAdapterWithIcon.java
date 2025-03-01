package com.nlm.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nlm.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sachin Nariya on 12-08-2022.
 */
public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

    private final List<Integer> images;

    public ArrayAdapterWithIcon(Context context, List<String> items, List<Integer> images) {
        super(context, R.layout.select_custom_dialog_item, items);
        this.images = images;
    }

    public ArrayAdapterWithIcon(Context context, String[] items, Integer[] images) {
        super(context, R.layout.select_custom_dialog_item, items);
        this.images = Arrays.asList(images);
    }

    public ArrayAdapterWithIcon(Context context, int items, int images) {
        super(context, R.layout.select_custom_dialog_item, context.getResources().getStringArray(items));

        final TypedArray imgs = context.getResources().obtainTypedArray(images);
        this.images = new ArrayList<Integer>() {{
            for (int i = 0; i < imgs.length(); i++) {
                add(imgs.getResourceId(i, -1));
            }
        }};

        // recycle the array
        imgs.recycle();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = view.findViewById(android.R.id.text1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(images.get(position), 0, 0, 0);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), 0, 0, 0);
        }
        /*textView.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12, getContext().getResources().getDisplayMetrics()));*/

        return view;
    }
}
