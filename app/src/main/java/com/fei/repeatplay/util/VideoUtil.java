package com.fei.repeatplay.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.widget.Toast;

import com.fei.repeatplay.bean.VideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoUtil {

    //获取指定文件夹下所有视频文件
    public static List<VideoInfo> getVideo(Context context, String fileAbsolutePath) {
        ArrayList<VideoInfo> list = new ArrayList<>();
        File file = new File(fileAbsolutePath);
        if (!file.exists()){
            Toast.makeText(context, "文件夹不存在", Toast.LENGTH_SHORT).show();
            return null;
        }
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                System.out.println("----name = "+filename);
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".mp4")||filename.trim().toLowerCase().endsWith(".3gp")||
                        filename.trim().toLowerCase().endsWith(".avi")||filename.trim().toLowerCase().endsWith(".flv")) {
                    VideoInfo info = new VideoInfo();
                    info.setPath(subFile[iFileLength].getPath());
                    info.setTitle(subFile[iFileLength].getName());
                    info.setDuration(Long.valueOf(getVideoDuration(subFile[iFileLength].getPath())));
                    list.add(info);
                }
            }
        }
        return list;
    }
    //根据路径得到视频缩略图
    public static Bitmap getVideoPhoto(String videoPath) {
        MediaMetadataRetriever media =new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }

    //获取视频总时长
    public static String getVideoDuration(String path){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); //
        return duration;
    }


}
