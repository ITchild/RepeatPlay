package com.fei.repeatplay.bean;

public class ListBean {
    private boolean isFile ;
    private String name;


    public boolean isFile() {
        //其它类型返回字段值本身
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getName() {
        //如果是String类型，那么判断是否为空，为空返回"",否则返回字段值本身
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
