package com.lau.ffmpegcommanddemo.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 适用于RecyclerView的ItemDecoration，用于设置Item之间的间距
 * <p>
 * 1、不支持非GridLayoutManager和StaggeredGridLayoutManager的情况，如果使用了LinearLayoutManager
 * ，请使用{@link android.support.v7.widget.DividerItemDecoration}
 * </p>
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int verticalSpace;
    private int horizontalSpace;
    private int headerCount;
    private boolean showTopBottomEdge = false;
    private boolean showLeftRightEdge = false;

    /**
     * 构造函数
     *
     * @param space 间距（px）
     */
    public GridItemDecoration(int space) {
        this(space, space, 0);
    }

    /**
     * 构造函数
     *
     * @param space       间距（px）
     * @param headerCount Header的个数
     */
    public GridItemDecoration(int space, int headerCount) {
        this(space, space, headerCount);
    }

    /**
     * 构造函数
     *
     * @param verticalSpace   垂直方向的间距（px）
     * @param horizontalSpace 水平方向的间距（px）
     * @param headerCount     Header的个数
     */
    public GridItemDecoration(int verticalSpace, int horizontalSpace, int headerCount) {
        this.verticalSpace = verticalSpace;
        this.horizontalSpace = horizontalSpace;
        this.headerCount = headerCount;
    }

    /**
     * 是否显示上下两边的间隔
     *
     * @param show true表示显示，false表示不显示
     */
    public void setShowTopBottomEdge(boolean show) {
        this.showTopBottomEdge = show;
    }

    /**
     * 是否显示左右两边的间隔
     *
     * @param show true表示显示，false表示不显示
     */
    public void setShowLeftRightEdge(boolean show) {
        this.showLeftRightEdge = show;
    }

    /**
     * 设置Header的个数
     *
     * @param headerCount Header的个数
     */
    public void setHeaderCount(int headerCount) {
        headerCount = headerCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);

        if (position < headerCount) {
            return; //忽略Header，否则计算不准确
        }

        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        int spanCount = -1;
        if (lm instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) lm).getSpanCount();
        } else if (lm instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) lm).getSpanCount();
        }

        if (spanCount >= 0) {
            //int childCount = parent.getChildCount();
            //int columnNumber = (int) Math.ceil((double) childCount / spanCount);
            int index = position - headerCount;
            int column = index % spanCount;

            if (showTopBottomEdge) {
                if (index < spanCount) {
                    outRect.top = verticalSpace;
                }
                outRect.bottom = verticalSpace;
            } else {
                if (index >= spanCount) {
                    outRect.top = verticalSpace;
                }
            }

            if (showLeftRightEdge) {
                outRect.left = horizontalSpace - column * horizontalSpace /
                        spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * horizontalSpace /
                        spanCount; // (column + 1) * ((1f / spanCount) * spacing)
            } else {
                outRect.left = column * horizontalSpace /
                        spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = horizontalSpace - (column + 1) * horizontalSpace /
                        spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            }
        }
    }
}
