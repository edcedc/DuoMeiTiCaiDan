package com.edc.dmt;

import java.io.File;
import java.util.Comparator;

/**
 * Created by edison on 2018/3/21.
 */

public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File file1, File file2) {
        return file1.getName().compareTo(file2.getName());
    }
}
