package org.zorgblub.rikai;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Pair;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AccentAudio {

    AccentDictionary accentDictionary;

    MediaPlayer mp = new MediaPlayer();
    Queue<String> mediaQueue = new LinkedList<String>();

    String accentAudioFolder = Environment.getExternalStorageDirectory().toString()+"/Typhon/accentAudio/";
    String accentDictJsonLocation = Environment.getExternalStorageDirectory().toString()+"/Typhon/fullAccDict_.json";

    String soundAtamadaka = "_a.mp3";
    String soundHeiban = "_h.mp3";
    String soundNakadaka = "_n.mp3";
    String soundOdaka = "_o.mp3";




    class Audio{
        private String soundFile;
        private String dropLocation;
        private Boolean isOdaka;
    }
    class DictionaryEntry{
        private ArrayList<Audio> audio;
        private ArrayList<ArrayList<String>> pitch;
    }
    class AccentDictionary{
        private HashMap<String, HashMap<String, DictionaryEntry>> doubleMap;

        public DictionaryEntry getDictionaryEntry(Pair<String, String> words){
            if (words.first.equals("No")) { return null; }

            System.out.println(words);
            Map<String, DictionaryEntry> secondLvl = doubleMap.get(words.first);
            if(secondLvl == null){
                secondLvl = doubleMap.get(words.second);
                if(secondLvl == null){
                    return null;
                }
            }
            if (secondLvl.size() == 1){
                return secondLvl.values().iterator().next();
            }
            if(secondLvl.containsKey(words.second)){
                return secondLvl.get(words.second);
            }
            if(secondLvl.containsKey(words.first)){
                return secondLvl.get(words.first);
            }
            return null;
        }
    }




    public AccentAudio(){
        //read the json accentDict document
        try {
            Gson gson = new Gson();
            File myFile = new File(accentDictJsonLocation);
            Reader reader = new BufferedReader(new FileReader(myFile));
            accentDictionary = gson.fromJson(reader, AccentDictionary.class);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playMediaQueue();
            }
        });
    }

    public void playMediaQueue(){
        if (mediaQueue.size() > 0){
            try {
                String fileName = mediaQueue.remove();
                System.out.println(fileName);
                mp.reset();
                mp.setDataSource(accentAudioFolder + fileName);
                mp.prepare();
                mp.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void playAudio(Pair<String, String> words){
        if (accentDictionary == null){ return; }
        DictionaryEntry entry = accentDictionary.getDictionaryEntry(words);
        if (entry == null){return;}

        mediaQueue.clear();
        if(entry.audio.size() >0){
            for(Audio audio : entry.audio){
                mediaQueue.add(audio.soundFile);
                if(audio.isOdaka){
                    mediaQueue.add(soundOdaka);
                }
            }

        }else{
            for (ArrayList<String> pitch : entry.pitch){
                //if double pitch like in 前代[1]未聞[0] then skip
                if (pitch.get(0) == null){ return; }//somehow got an error for fetching null even if len was 1
                if(pitch.size() == 1){
                    switch(pitch.get(0)){
                        case "頭高":
                            mediaQueue.add(soundAtamadaka);
                            break;

                        case "平板":
                            mediaQueue.add(soundHeiban);
                            break;

                        case "尾高":
                            mediaQueue.add(soundOdaka);
                            break;

                        case "中高":
                            mediaQueue.add(soundNakadaka);
                            break;

                    }
                }
            }
        }

        playMediaQueue();

    }
}
