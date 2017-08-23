package com.pipikou.library;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * ItemDecoration for Stick Header.
 *
 *  使用时只需要将你的Adapter实现AdapterStick接口即可
 *  也就是主要实现这个方法isPinnedViewType()返回需要固定的Type
 *  如果存在多个Header情况请实现getHeaderCount()这个方法并返回Header数量总长度
 * @author wubo
 */
public class PinnedHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private final static String TAG = PinnedHeaderItemDecoration.class.getSimpleName();

    /**
     * 当前绘制的pinnedheaderview
     */
    View mPinnedHeaderView = null;

    /**
     * pinnedheaderview的位置
     */
    int mHeaderPosition = -1;

    /**
     * 装载所有的viewtype
     */
    Map<Integer, Boolean> mPinnedViewTypes = new HashMap<Integer, Boolean>();

    /**
     * pinnedheaderview的上边距
     */
    private int mPinnedHeaderTop;

    /**
     * pinnedheaderview的裁剪区域
     */
    private Rect mClipBounds;
    private Builder mBuilder;
    private int mFirstVisiblePosition;

    private PinnedHeaderItemDecoration(Builder builder) {

        mBuilder = builder;
    }

    /**
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        createPinnedHeader(parent);

        if (mPinnedHeaderView != null) {
            // check overlap section view.
            //TODO support only vertical header currently.
            final int headerEndAt = mPinnedHeaderView.getTop() + mPinnedHeaderView.getHeight() + 1;
            final View v = parent.findChildViewUnder(c.getWidth() / 2, headerEndAt);
            if (isPinnedView(parent, v)) {
                mPinnedHeaderTop = v.getTop() - mPinnedHeaderView.getHeight();
            } else {
                mPinnedHeaderTop = 0;
            }

            if(isHeaderView(mFirstVisiblePosition)){
                return;
            }
            mClipBounds = c.getClipBounds();
            mClipBounds.top = mPinnedHeaderTop + mPinnedHeaderView.getHeight();
            c.clipRect(mClipBounds);
        }
    }


    /**
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mPinnedHeaderView != null && !isHeaderView(mFirstVisiblePosition)) {
            c.save();

            mClipBounds.top = 0;
            c.clipRect(mClipBounds, Region.Op.UNION);
            c.translate(0, mPinnedHeaderTop);
            mPinnedHeaderView.draw(c);

            c.restore();
        }
    }

    private void createPinnedHeader(RecyclerView parent) {
        checkCache(parent);

        // get LinearLayoutManager.
        final LinearLayoutManager linearLayoutManager = getLayoutManager(parent);
        if (linearLayoutManager == null) return;
        mFirstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();

        final int headerPosition = findPinnedHeaderPosition(mFirstVisiblePosition);

        if(isHeaderView(mFirstVisiblePosition)){
            return;
        }

        if (headerPosition >= 0 && mHeaderPosition != headerPosition) {
            mHeaderPosition = headerPosition;
            final int viewType = mBuilder.mStickProvider.getItemViewType(headerPosition);

            final RecyclerView.ViewHolder pinnedViewHolder = mBuilder.mStickProvider.createViewHolder(parent, viewType);
            mBuilder.mStickProvider.bindViewHolder(pinnedViewHolder, headerPosition);
            mPinnedHeaderView = pinnedViewHolder.itemView;

            // read layout parameters
            ViewGroup.LayoutParams layoutParams = mPinnedHeaderView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPinnedHeaderView
                        .setLayoutParams(layoutParams);
            }


            int heightMode = View.MeasureSpec.getMode(layoutParams.height);
            int heightSize = View.MeasureSpec.getSize(layoutParams.height);

            if (heightMode == View.MeasureSpec.UNSPECIFIED) {
                heightMode = View.MeasureSpec.EXACTLY;
            }

            final int maxHeight = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
            if (heightSize > maxHeight) {
                heightSize = maxHeight;
            }

            // measure & layout
            final int ws = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
            final int hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            mPinnedHeaderView.measure(ws, hs);

            mPinnedHeaderView.layout(0, 0, mPinnedHeaderView.getMeasuredWidth(), mPinnedHeaderView.getMeasuredHeight());
        }
    }

    /**目前只支持LinearLayoutManger类型*/
    private LinearLayoutManager getLayoutManager(RecyclerView parent) {
        final LinearLayoutManager linearLayoutManager;
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) layoutManager;
        } else {
            return null;
        }
        return linearLayoutManager;
    }

    /**
     * return the first visible view position is headerview
     * @param firstVisiblePosition first visible view position
     */
    private boolean isHeaderView(int firstVisiblePosition) {
        final int position = firstVisiblePosition - mBuilder.mStickProvider.getHeaderCount();
        if (position < 0) {
            return true;
        }
        return false;
    }


    private int findPinnedHeaderPosition(int fromPosition) {
        if (fromPosition > mBuilder.mStickProvider.getItemCount()) {
            return -1;
        }

        for (int position = fromPosition; position >= 0; position--) {
            final int viewType = mBuilder.mStickProvider.getItemViewType(position);
            if (isPinnedViewType(viewType)) {
                return position;
            }
        }

        return -1;
    }

    private boolean isPinnedViewType(int viewType) {
        if (!mPinnedViewTypes.containsKey(viewType)) {
            mPinnedViewTypes.put(viewType, mBuilder.mStickProvider.isPinnedViewType(viewType));
        }
        return mPinnedViewTypes.get(viewType);
    }

    private boolean isPinnedView(RecyclerView parent, View v) {
        final int position = parent.getChildAdapterPosition(v) - mBuilder.mStickProvider.getHeaderCount();
        if (position == RecyclerView.NO_POSITION) {
            return false;
        }
        final int viewType = mBuilder.mStickProvider.getItemViewType(position);

        return isPinnedViewType(viewType);
    }

    private void checkCache(RecyclerView parent) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (mBuilder.mStickProvider != adapter) {
            disableCache();
        }
    }




    private void disableCache() {
        mPinnedHeaderView = null;
        mHeaderPosition = -1;
        mPinnedViewTypes.clear();
    }

    public static class Builder {

        private AdapterStick mStickProvider;
        private PinnedHeaderItemDecoration mPinnedHeaderItemDecoration;

        public Builder Builder() {
            return new Builder();
        }

        public Builder adapterProvider(AdapterStick stickProvider) {
            mStickProvider = stickProvider;
            return this;
        }

        public PinnedHeaderItemDecoration build() {
            if (mPinnedHeaderItemDecoration == null) {
                mPinnedHeaderItemDecoration = new PinnedHeaderItemDecoration(this);
            }
            return mPinnedHeaderItemDecoration;
        }
    }

}
