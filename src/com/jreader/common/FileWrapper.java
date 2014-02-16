package com.jreader.common;

import java.io.File;

public class FileWrapper implements Comparable<Object> {

  /** File */
    private File file;
    
    public FileWrapper(File file) {
        this.file = file;
    }
    
    //µ¹ÐòÅÅÐò
    public int compareTo(Object obj) {        
        
        FileWrapper castObj = (FileWrapper)obj;
                
        if (this.file.getName().compareTo(castObj.getFile().getName()) > 0) {
            return 1;
        } else if (this.file.getName().compareTo(castObj.getFile().getName()) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
    
    public File getFile() {
        return this.file;
    }

}
