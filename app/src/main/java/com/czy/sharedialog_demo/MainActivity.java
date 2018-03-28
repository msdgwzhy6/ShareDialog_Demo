package com.czy.sharedialog_demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/3/28 20:30
 * 描述：https://github.com/leavesC
 */
public class MainActivity extends AppCompatActivity {

    private List<App> appList;

    private BottomSheetDialog bottomSheetDialog;

    private Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/leavesC");
    }

    public void originalShare(View view) {
        Intent intent = Intent.createChooser(shareIntent, "分享一段文本信息");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void customizedShare(View view) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);
            initBottomDialog();
        }
        bottomSheetDialog.show();
    }

    private void initBottomDialog() {
        appList = getShareAppList(this, shareIntent);
        AppShareAdapter appShareAdapter = new AppShareAdapter(this, appList);
        appShareAdapter.setClickListener(new AppShareAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setComponent(new ComponentName(appList.get(position).getPackageName(), appList.get(position).getMainClassName()));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/leavesC");
                startActivity(intent);
            }
        });
        appShareAdapter.setLongClickListener(new AppShareAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + appList.get(position).getPackageName()));
                startActivity(intent);
            }
        });
        RecyclerView rv_appList = bottomSheetDialog.findViewById(R.id.rv_appList);
        if (rv_appList != null) {
            rv_appList.setLayoutManager(new GridLayoutManager(this, 4));
            rv_appList.setAdapter(appShareAdapter);
        }
    }

    public static List<App> getShareAppList(Context context, Intent intent) {
        List<App> appList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        if (resolveInfoList == null || resolveInfoList.size() == 0) {
            return null;
        } else {
            for (ResolveInfo resolveInfo : resolveInfoList) {
                App appInfo = new App(resolveInfo.loadLabel(packageManager).toString(), resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name, resolveInfo.loadIcon(packageManager));
                appList.add(appInfo);
            }
        }
        return appList;
    }

}