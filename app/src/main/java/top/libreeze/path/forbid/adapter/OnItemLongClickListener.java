package top.libreeze.path.forbid.adapter;

import android.view.View;

public interface OnItemLongClickListener<T> {
    void longClick(View view, int position, T data);
}
