package kawa.ttehaute.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NextU on 16/02/2018.
 */

public class Way {

    private List<Noeud> listeDeNoeud;
    private Map<String,String> listeDeTag;
    private long id;
    public boolean isAHighway = false;
    public double vitesse = 5;

    public Way(long id){
        this.id = id;
        this.listeDeNoeud = new ArrayList<Noeud>();
        this.listeDeTag = new HashMap<String,String>();
    }

    public Way(){
        this.listeDeNoeud = new ArrayList<Noeud>();
        this.listeDeTag = new HashMap<String,String>();

    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public List<Noeud> getListeDeNoeud(){
        return this.listeDeNoeud;
    }

    public void setListeDeNoeud(List<Noeud> l){
        this.listeDeNoeud = l;
    }

    public void addNoeud(Noeud n){
        if(this.listeDeNoeud!=null){
            this.listeDeNoeud.add(n);
        }
    }

    public Map<String,String> getListeDeTag(){
        return this.listeDeTag;
    }

    public void setListeDeTag(Map<String,String> m){
        this.listeDeTag = m;
    }

    public void addTag(String key, String value){
        if(this.listeDeTag!=null){
            if(key.equals("highway")){
                this.isAHighway = true ;
                setSpeed(value);
            }
            this.listeDeTag.put(key,value);
        }
    }

    private void setSpeed(String highwayType){
        if(highwayType.equals("motorway")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_MOTORWAY;
        }else if(highwayType.equals("trunk")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_TRUNK;
        }else if(highwayType.equals("primary")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_PRIMARY;
        }else if(highwayType.equals("secondary")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_SECONDARY;
        }else if(highwayType.equals("tertiary")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_TERTIARY;
        }else if(highwayType.equals("road")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_ROAD;
        }else if(highwayType.equals("unclassified")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_UNCLASSIFIED;
        }else if(highwayType.equals("residential")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_RESIDENTIAL;
        }else if(highwayType.equals("living_street")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_LIVING_STREET;
        }else if(highwayType.equals("service")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_SERVICE;
        }else if(highwayType.equals("motorway_link")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_MOTORWAY_LINK;
        }else if(highwayType.equals("trunk_link")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_TRUNK_LINK;
        }else if(highwayType.equals("primary_link")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_PRIMARY_LINK;
        }else if(highwayType.equals("secondary_link")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_SECONDARY_LINK;
        }else if(highwayType.equals("tertiary_link")){
            this.vitesse = ConvertisseurDeVitesse.VITESSE_TERTIARY_LINK;
        }
    }
}
