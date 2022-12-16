package com.arche.ncmrecoverui;

import com.arche.ncmrecover.MusicData;
import com.arche.ncmrecover.NCMRecover;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    private static  Converter instance;

    public static Converter getInstance() {
        if(instance==null)
            instance=new Converter();
        return instance;
    }

    private List<CallBack> callBackList=new ArrayList<>();
    public void addCallBack(CallBack c){
        callBackList.add(c);
    }
    public interface CallBack{
        void onConvertSucceed(int state, Music music);
        void onConvertFailed(int state);
        void onConvertStart(int state);
    }

    public void submit(URL url,int state){
        Thread thread=new Thread(()->{
            try{
                for (CallBack c:
                        callBackList) {
                    c.onConvertStart(state);

                }
                MusicData data=NCMRecover.Dump(url);
                for (CallBack c:
                        callBackList) {
                    c.onConvertSucceed(state, new Music(data));
                }
            }catch (Exception ex){
                for (CallBack c:
                        callBackList) {
                    c.onConvertFailed(state);
                }
            }

        });
        thread.start();
    }


}
