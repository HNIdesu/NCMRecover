package com.arche.ncmrecoverui;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.io.*;
import java.net.MalformedURLException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.instance=this;
        bindViews();
    }

    private void bindViews(){
        MyAdapter adapter=new MyAdapter();
        findViewById(R.id.button_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu=new PopupMenu(MainActivity.this, findViewById(R.id.button_menu));
                menu.getMenuInflater().inflate(R.menu.menu_popup, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.option_exit:
                                System.exit(0);
                            case R.id.option_scan_directory:
                                adapter.getItemList().clear();
                                for (Item it: DataProveder.getInstance().getItems()) {
                                        adapter.getItemList().add(it);
                                }
                                adapter.notifyDataSetChanged();
                                break;
                            case R.id.option_about:
                            {

                                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Author:\t\tHNIdesu\r\nGithub:\t\thttps://github.com/HNIdesu/NCMRecover");
                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.show();
                                break;
                            }
                            default:
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        Converter.getInstance().addCallBack(adapter);
        findViewById(R.id.button_convert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Item> list=adapter.getItemList();
                for(int i=0;i<list.size();i++){
                    File f=list.get(i).file;
                    try {
                        Converter.getInstance().submit(f.toURL(), i);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        });

    }





}