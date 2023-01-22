package com.arche.ncmrecoverui;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.arche.ncmrecover.MusicData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Converter.CallBack {

    private ArrayList<Item> itemList=new ArrayList();

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    @Override
    public void onConvertSucceed(int state, Music music) {
        try {
            music.save(MainActivity.getInstance().getExternalCacheDir().getAbsolutePath()+"/"+itemList.get((int)state).name+"."+music.format);
            MainActivity.getInstance().runOnUiThread(()->{
                Item item=itemList.get(state);
                item.status= Item.Status.Completed;
                if(music.albumImage!=null)
                    item.icon= BitmapFactory.decodeByteArray(music.albumImage,0,music.albumImage.length);
                notifyDataSetChanged();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConvertFailed(int state) {
        MainActivity.getInstance().runOnUiThread(()->{
            itemList.get(state).status= Item.Status.Failed;
            notifyDataSetChanged();
        });
    }

    @Override
    public void onConvertStart(int state) {
        MainActivity.getInstance().runOnUiThread(()->{
            itemList.get(state).status= Item.Status.Converting;
            notifyDataSetChanged();
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView_name;
        private TextView textView_status;
        private ImageView imageView_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name=itemView.findViewById(R.id.textView_fileName);
            textView_status=itemView.findViewById(R.id.textView_status);
            imageView_icon=itemView.findViewById(R.id.imageView_icon);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(MainActivity.getInstance()).inflate(R.layout.layout_single_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item=itemList.get(position);
        holder.textView_name.setText(item.name);
        switch (item.status){
            case Completed:
                holder.textView_status.setText("Completed");
                holder.textView_status.setTextColor(ResourcesCompat.getColor(MainActivity.getInstance().getResources(),R.color.green, null));
                break;
            case Failed:
                holder.textView_status.setText("Failed");
                holder.textView_status.setTextColor(ResourcesCompat.getColor(MainActivity.getInstance().getResources(),R.color.red, null));
                break;
            default:
                holder.textView_status.setText(item.status.toString());
                holder.textView_status.setTextColor(ResourcesCompat.getColor(MainActivity.getInstance().getResources(),R.color.black, null));
                break;
        }
        holder.imageView_icon.setImageBitmap(item.icon);


    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
