package com.fei.repeatplay.util;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File file1, File file2) {
        return file2.getName().compareTo(file1.getName());
    }
}

