package com.arche.ncmrecoverui;

import android.graphics.Bitmap;

import java.io.File;

public class Item {
    public  String name;
    public Status status;
    public Bitmap icon;
    public File file;
    public enum Status{Wait,Converting,Completed,Failed}
}
