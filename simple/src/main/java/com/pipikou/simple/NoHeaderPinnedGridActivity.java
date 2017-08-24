package com.pipikou.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pipikou.library.AdapterStick;
import com.pipikou.library.PinnedHeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class NoHeaderPinnedGridActivity extends AppCompatActivity {

    private int titleCount;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ArrayList<SimpleBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SimpleBean simpleBean = new SimpleBean();
            if(i%10==0){
                simpleBean.setType(SimpleBean.TYPE_TITLE);
                simpleBean.setTitle(String.valueOf(titleCount++)+"月");
            }else{
                simpleBean.setType(SimpleBean.TYPE_NROMAL);
                simpleBean.setText("i="+i);
            }
            list.add(simpleBean);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list);
        mRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration.Builder().adapterProvider(simpleAdapter).build());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(simpleAdapter);

    }

    public static void GoToThreeHeaderActivity(Context context){
        Intent intent = new Intent(context, NoHeaderPinnedGridActivity.class);
        context.startActivity(intent);
    }

    static class SimpleAdapter extends Adapter implements AdapterStick{
        private Context mContext;
        private List<SimpleBean> mList;

        private int count;
       // private SparseArray<View> headers=new SparseArray<>();
        public SimpleAdapter(Context context,List <SimpleBean>list){
            mContext = context;
            mList = list;
           /* ViewGroup content = (ViewGroup) ((NoHeaderPinnedGridActivity) mContext).findViewById(android.R.id.content);
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.head_layout, content, false);
            View inflate1 = LayoutInflater.from(mContext).inflate(R.layout.head_layout, content, false);
            View inflate2 = LayoutInflater.from(mContext).inflate(R.layout.head_layout, content, false);
            headers.put((SimpleBean.TYPE_HEADER+(count++)),inflate);
            headers.put((SimpleBean.TYPE_HEADER+(count++)),inflate1);
            headers.put((SimpleBean.TYPE_HEADER+(count++)),inflate2);*/
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager
                        .getSpanSizeLookup();
                gridLayoutManager
                        .setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                int viewType = getItemViewType(position);
                                if (viewType == SimpleBean.TYPE_TITLE||viewType == SimpleBean.TYPE_HEADER) {
                                    return gridLayoutManager.getSpanCount();
                                } else {
                                    return 1;
                                }
                            }
                        });
                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }

        @Override
        public int getItemViewType(int position) {
            int realPosition=position;
            SimpleBean simpleBean = mList.get(realPosition);
            return simpleBean.getType();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder;
            if(viewType==SimpleBean.TYPE_TITLE){
                viewHolder=new SimpleTitleHolder(LayoutInflater.from(mContext).inflate(R.layout.title_item_layout,parent,false));
            }else {
                viewHolder=new SimpleHolder(LayoutInflater.from(mContext).inflate(R.layout.normal_item_layout,parent,false));
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(getItemViewType(position)==SimpleBean.TYPE_HEADER+position){
              return;
            }
            final SimpleBean simpleBean = mList.get(position);
            if(getItemViewType(position)==SimpleBean.TYPE_TITLE){
                SimpleTitleHolder simpleTitleHolder= (SimpleTitleHolder) holder;
                simpleTitleHolder.mTitle.setText(simpleBean.getTitle());
                final int layoutPosition = holder.getLayoutPosition();
                simpleTitleHolder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext,"position:"+layoutPosition+" title:"+simpleBean.getTitle(),Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                SimpleHolder simpleHolder= (SimpleHolder) holder;
                simpleHolder.mText.setText(simpleBean.getText());
                final int layoutPosition = holder.getLayoutPosition();
                simpleHolder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext,"position:"+layoutPosition+" normal:"+simpleBean.getText(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getHeaderCount() {
            return 0;
        }

        @Override
        public boolean isPinnedViewType(int viewType) {
            return viewType==SimpleBean.TYPE_TITLE;
        }
    }
    static class SimpleHeaderHolder extends ViewHolder{

        public SimpleHeaderHolder(View itemView) {
            super(itemView);
        }
    }
    static class SimpleTitleHolder extends ViewHolder{

        public TextView mTitle;

        public SimpleTitleHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
        }
    }

    static class SimpleHolder extends ViewHolder{

        public   ImageView mHead;
        public TextView mText;

        public SimpleHolder(View itemView) {
            super(itemView);
            mHead = itemView.findViewById(R.id.head);
            mText = itemView.findViewById(R.id.text);
        }
    }

}
