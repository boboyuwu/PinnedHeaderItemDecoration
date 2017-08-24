package com.pipikou.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.pipikou.library.AdapterStick;
import com.pipikou.library.PinnedHeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boboyuwu on 2017/8/23.
 */

public class NoHeaderPinnedActivity extends AppCompatActivity {
    private int titleCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
        recyclerView.addItemDecoration(new PinnedHeaderItemDecoration.Builder().adapterProvider(simpleAdapter).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(simpleAdapter);
    }

    public static void GoToSecondActivity(Context context){
        Intent intent = new Intent(context, NoHeaderPinnedActivity.class);
        context.startActivity(intent);
    }


    static class SimpleAdapter extends Adapter implements AdapterStick {
        private Context mContext;
        private List<SimpleBean> mList;

       // private ArrayList<View> headers=new ArrayList();
        public SimpleAdapter(Context context,List <SimpleBean>list){
            mContext = context;
            mList = list;
            ImageView imageView = new ImageView(mContext);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 400);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.mipmap.caijingzaocan);
           // headers.add(imageView);
        }

        @Override
        public int getItemViewType(int position) {
            SimpleBean simpleBean = mList.get(position);
            return simpleBean.getType();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder;
             if(viewType==SimpleBean.TYPE_TITLE){
                viewHolder=new SimpleTitleHolder(LayoutInflater.from(mContext).inflate(R.layout.title_item_layout,parent,false));
            }else{
                viewHolder=new SimpleHolder(LayoutInflater.from(mContext).inflate(R.layout.normal_item_layout,parent,false));
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.e("NoHeaderPinnedGridActivity","position:"+position+":"+getItemViewType(position));
            if(getItemViewType(position)==SimpleBean.TYPE_HEADER){
                return;
            }
            SimpleBean simpleBean = mList.get(position);
            if(getItemViewType(position)==SimpleBean.TYPE_TITLE){
                SimpleTitleHolder simpleTitleHolder= (SimpleTitleHolder) holder;
                simpleTitleHolder.mTitle.setText(simpleBean.getTitle());
            }else{
                SimpleHolder simpleHolder= (SimpleHolder) holder;
                simpleHolder.mText.setText(simpleBean.getText());
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
    static class SimpleHeaderHolder extends ViewHolder {

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

        public ImageView mHead;
        public TextView mText;

        public SimpleHolder(View itemView) {
            super(itemView);
            mHead = itemView.findViewById(R.id.head);
            mText = itemView.findViewById(R.id.text);
        }
    }
}
