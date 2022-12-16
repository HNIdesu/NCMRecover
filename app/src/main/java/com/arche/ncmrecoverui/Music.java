package com.arche.ncmrecoverui;

import com.arche.ncmrecover.MusicData;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.flac.FlacFileWriter;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.mp3.MP3FileWriter;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.ogg.OggFileWriter;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.AndroidArtwork;
import org.jaudiotagger.tag.images.Artwork;
import org.json.*;
import org.jaudiotagger.audio.AudioFile;
import java.io.*;
import java.net.URL;

public class Music {
    public String title;
    public String[] artists;
    public String format;
    public String album;
    public byte[] albumImage;
    public byte[] data;

    public Music(MusicData data){
        try {
            JSONObject obj=new JSONObject(data.metaInfo);
            this.format=obj.getString("format");
            this.data=data.data;
            this.title=obj.getString("musicName");
            JSONArray arr=obj.getJSONArray("artist");
            artists=new String[arr.length()];
            for(int i=0;i<arr.length();i++)
                artists[i]=((JSONArray)arr.get(i)).get(0).toString();
            this.album=obj.getString("album");
            InputStream stream= new URL(obj.getString("albumPic") ).openStream();
            ByteArrayOutputStream buffer=new ByteArrayOutputStream();
            for(int b=stream.read();b!=-1;b=stream.read()){
                buffer.write(b);
            }
            this.albumImage=buffer.toByteArray();
            stream.close();
            buffer.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String joinString(String[] arr,String split){
        String str="";
        for(int i=0;i<arr.length;i++){
            if(i!=0)
                str+=split;
            str+=arr[i];
        }
        return str;
    }
    public void save(String filePath) throws Exception {
        File fl=new File(filePath);
        FileOutputStream fs=new FileOutputStream(fl);
        fs.write(this.data);
        fs.close();

        if(format.equals("mp3")){
            writeInfo(fl,new MP3FileReader(),new MP3FileWriter());
        }else if(format.equals("flac")){
            writeInfo(fl,new FlacFileReader(),new FlacFileWriter());
        }else if(format.equals("ogg")){
            writeInfo(fl,new OggFileReader(),new OggFileWriter());
        }
    }

    private void writeInfo(File f, AudioFileReader reader, AudioFileWriter writer) throws Exception{
        AudioFile af= reader.read(f);
        Tag tag= af.getTag();

        if(this.title!=null)
            tag.setField(FieldKey.TITLE,this.title );
        if(this.artists.length!=0)
            tag.setField(FieldKey.ARTIST,joinString(this.artists, ","));
        if(this.album!=null)
            tag.setField(FieldKey.ALBUM,this.album );
        try{
            if(this.albumImage!=null){
                Artwork artwork= AndroidArtwork.createArtworkFromBinaryData(albumImage);
                tag.setField(artwork);
            }
        }catch (Exception ex){

        }
        writer.write(af);
    }

}
