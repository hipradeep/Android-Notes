package com.lmwdelivery.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private boolean controlsVisible = true;
    private int current_page = 1;
    public boolean loading = true;
    private int previousTotal = 0;
    private int scrolledDistance = 0;

    public abstract void onHide();

    public abstract void onLoadMore(int i);

    public abstract void onShow();

    protected HidingScrollListener(LinearLayoutManager layoutManager) {
    }

    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisibleItem == 0) {
            if (!this.controlsVisible) {
                onShow();
                this.controlsVisible = true;
            }
        } else if (this.scrolledDistance > 15 && this.controlsVisible) {
            onHide();
            this.controlsVisible = false;
            this.scrolledDistance = 0;
        } else if (this.scrolledDistance < -15 && !this.controlsVisible) {
            onShow();
            this.controlsVisible = true;
            this.scrolledDistance = 0;
        }
        if ((this.controlsVisible && dy > 0) || (!this.controlsVisible && dy < 0)) {
            this.scrolledDistance += dy;
        }
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        if (this.loading && totalItemCount > this.previousTotal) {
            this.loading = false;
            this.previousTotal = totalItemCount;
        }
        int visibleThreshold = 2;
        if (!this.loading && totalItemCount - visibleItemCount <= visibleThreshold + firstVisibleItem) {
            this.current_page++;
            onLoadMore(this.current_page);
            this.loading = true;
        }
    }
}
