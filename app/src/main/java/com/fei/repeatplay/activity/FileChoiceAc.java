package com.fei.repeatplay.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fei.repeatplay.util.FileComparator;
import com.fei.repeatplay.R;
import com.fei.repeatplay.adapter.ChoiceFileAdapter;
import com.fei.repeatplay.util.SharesPreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileChoiceAc extends AppCompatActivity {

    private RecyclerView filech_dis_rlv;
    private ChoiceFileAdapter mChoiceFileAdapter;

    private List<File> data;

    private String path ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT ;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP;//设置对话框置顶显示
        win.setAttributes(lp);
        setContentView(R.layout.ac_filechoice);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        filech_dis_rlv = findViewById(R.id.filech_dis_rlv);
    }

    private void initData(){
        if(null == data){
            data = new ArrayList<>();
        }
        path = SharesPreUtil.getPath();
        if(path == null || path.equals("")) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        filech_dis_rlv.setLayoutManager(new LinearLayoutManager(this));
        mChoiceFileAdapter = new ChoiceFileAdapter(this,data);
        filech_dis_rlv.setAdapter(mChoiceFileAdapter);

        getPathList(path);
    }

    private void initListener() {
        findViewById(R.id.filech_Ok_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("file",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("path",path);
                editor.commit();
                finish();
            }
        });

        findViewById(R.id.filech_back_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(path.equals(Environment.getExternalStorageDirectory().getAbsolutePath())){
                    showMsg("已经没有上层目录了 ^*^");
                }else {
                    path = path.substring(0, path.lastIndexOf("/"));
                    getPathList(path);
                }
            }
        });

        mChoiceFileAdapter.setOnItemFolderClick(new ChoiceFileAdapter.OnItemFolderClick() {
            @Override
            public void onFolderClick(int positon) {
                path = data.get(positon).getAbsolutePath();
                getPathList(path);
            }
        });
    }

    private void getPathList(String path){
        File pathFile = new File(path);
        File[] files = pathFile.listFiles();
        List<File> falgFileList = new ArrayList<>();
        data.clear();
        for (File flagFile : files){
            if(flagFile.isFile()){
                falgFileList.add(flagFile);
            }else {
                data.add(flagFile);
            }
        }
        Collections.sort(data,new FileComparator());
        data.addAll(falgFileList);
        mChoiceFileAdapter.setData(data);
    }

    private void showMsg(String msg){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("提示");
        dialog.setMessage(msg);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
