package com.czy.sharedialog_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/3/28 20:30
 * 描述：https://github.com/leavesC
 */
public class AppShareAdapter extends RecyclerView.Adapter<AppShareAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClick(int position);
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    private List<App> appList;

    private LayoutInflater layoutInflater;

    private OnClickListener clickListener;

    private OnLongClickListener longClickListener;

    AppShareAdapter(Context context, List<App> appList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.appList = appList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_app, parent, false);
        return new AppShareAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.iv_appIcon.setBackground(appList.get(position).getAppIcon());
        holder.tv_appName.setText(appList.get(position).getAppName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(holder.getAdapterPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onLongClick(holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void setLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_appIcon;

        private TextView tv_appName;

        ViewHolder(View itemView) {
            super(itemView);
            iv_appIcon = itemView.findViewById(R.id.iv_appIcon);
            tv_appName = itemView.findViewById(R.id.tv_appName);
        }

    }

}
