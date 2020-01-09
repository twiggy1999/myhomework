package model;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.lang.reflect.InvocationHandler;
//存储".txt"文件或者选择".txt"文件
public class FileFilterModel extends FileFilter {
    public String getDescription() {
        return "*.txt;";
    }

    public boolean accept(File file) {
        String name = file.getName();
        return file.isDirectory() || name.toLowerCase().endsWith(".txt");
    }
}
