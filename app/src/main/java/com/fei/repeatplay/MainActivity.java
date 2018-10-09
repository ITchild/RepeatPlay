package com.fei.repeatplay;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fei.repeatplay.adapter.MainPlayListAdapter;
import com.fei.repeatplay.bean.VideoInfo;
import com.fei.repeatplay.util.SharesPreUtil;
import com.fei.repeatplay.util.VideoUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // 声明一个数组，用来存储所有需要动态申请的权限
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    private AlertDialog permissionDialog;
    private boolean isNotShowAgin = false;

    private TextView main_path_tv;

    private RecyclerView main_list_rlv;
    private MainPlayListAdapter adapter;
    private List<VideoInfo> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
        String path = SharesPreUtil.getPath();
        main_path_tv.setText(null == path ? getResources().getString(R.string.str_noFile) : path);
        List<VideoInfo> flagData = VideoUtil.getVideo(this,main_path_tv.getText().toString());
        if(null == flagData){
            return;
        }
        data.addAll(flagData);
        if(null != data) {
            adapter.setData(data);
        }
    }

    private void initView(){
        main_path_tv = findViewById(R.id.main_path_tv);
        main_list_rlv = findViewById(R.id.main_list_rlv);
        findViewById(R.id.main_choice_bt).setOnClickListener(this);
    }

    private void initData(){
        if(null == data){
            data = new ArrayList<>();
        }
        adapter = new MainPlayListAdapter(this,data);
        main_list_rlv.setLayoutManager(new GridLayoutManager(this,2));
        main_list_rlv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_choice_bt : // 选择文件目录
                startActivity(new Intent(this,FileChoiceAc.class));
                break;
        }
    }

    private void checkPermission() {
        //判断哪些权限未授予以便必要的时候重新申请
        mPermissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }
        //判断存储委授予权限的集合是否为空
        if (null != mPermissionList && mPermissionList.size() > 0) {
            // 后续操作...
            if(null == permissionDialog) {
                permissionDialog = new AlertDialog.Builder(this).create();
                permissionDialog.setCancelable(false);
                permissionDialog.setTitle("提示");
                permissionDialog.setMessage("为了软件的正常运行\n请您同意软件的使用权限");
                permissionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                permissionDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (isNotShowAgin || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            getAppDetailSettingIntent();
                        } else {
                            getPermission();
                        }
                    }
                });
            }
            if(!permissionDialog.isShowing()) {
                permissionDialog.show();
            }
        } else {//未授予的权限为空，表示都授予了

        }
    }

    /**
     * 适配6.0以上，获取动态权限
     */
    private void getPermission() {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }


    /**
     *  以下代码可以跳转到应用详情，可以通过应用详情跳转到权限界面(6.0系统测试可用)
     */
    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                isNotShowAgin = false;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.
                                shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (showRequestPermission) {
                            // 后续操作...
                        } else { //拒绝再次询问
                            isNotShowAgin = true;
                        }
                    }
                }
                checkPermission();
                // 授权结束后的后续操作...
                break;
            default:
                break;
        }
    }
}
