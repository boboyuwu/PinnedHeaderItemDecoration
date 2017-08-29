package com.pipikou.library;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

/**
 * Created by wubo on 2017/8/22.
 */

public interface AdapterStick<T extends ViewHolder> extends Stick{
    void onBindViewHolder(T holder, int position);
    ViewHolder createViewHolder(ViewGroup parent, int viewType);
    int getItemViewType(int position);
    int getItemCount();
    void bindViewHolder(T holder, int position);
    int getHeaderCount();
}
