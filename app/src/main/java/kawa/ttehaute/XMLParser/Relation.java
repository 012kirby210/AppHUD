package kawa.ttehaute.XMLParser;

import android.nfc.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NextU on 16/02/2018.
 */

public class Relation {

    private long id;
    private List<Member> listeDesMembres;
    private Map<String,String> listedesTags ;

    public Relation(long id){
        this.id = id ;
        this.listeDesMembres = new ArrayList<Member>();
        this.listedesTags = new HashMap<String,String>();
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public void addMember(Member m){
        this.listeDesMembres.add(m);
    }

    public List<Member> getListeDesMembres(){
        return this.listeDesMembres;
    }

    public void addTag(String key, String value){
        this.listedesTags.put(key, value);
    }

    public Map<String,String> getListedesTags(){
        return this.listedesTags;
    }

}
