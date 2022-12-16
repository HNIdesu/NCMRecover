package com.arche.ncmrecoverui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static DataBase instance;

    public static DataBase getInstance() {
        if(instance==null)
            instance=new DataBase();
        return instance;
    }
    public List<Item> getItems(){
        File dir= MainActivity.getInstance().getExternalCacheDir();
        List<Item> itemList=new ArrayList<>();
        File[] list= dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if(s.endsWith(".ncm"))
                    return true;
                return false;
            }
        });
        for (File f:
             list) {
            Item item=new Item();
            item.name=removeExt(f.getName()) ;

            item.status=Item.Status.Wait;
            item.file=f;
            itemList.add(item);
        }
        return  itemList;
    }
    private String removeExt(String str){
        int st=str.lastIndexOf('.');
        return str.substring(0,st);
    }
}

