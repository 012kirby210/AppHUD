package kawa.ttehaute.XMLParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NextU on 16/02/2018.
 */

public class Noeud {

    private double latitude ;
    private double longitude ;
    private double elevation ;
    private WayWrapper listeDesWaysContenantLeNoeud;
    private long id ;

    public Noeud(){}

    public Noeud(double latitude, double longitude, long id){
        this.latitude = latitude ;
        this.longitude = longitude ;
        this.id = id ;
        this.listeDesWaysContenantLeNoeud = new WayWrapper();
    }

    public void setLatitude( double latitude){
        this.latitude = latitude ;
    }

    public double getLatitude(){
        return this.latitude ;
    }

    public void setLongitude( double longitude ){
        this.longitude = longitude ;
    }

    public double getLongitude(){
        return this.longitude ;
    }

    public void setId(long id){
        this.id = id ;
    }

    public long getId(){
        return this.id ;
    }

    public List<Way> getListeDesWaysContenantLeNoeud(){
        return this.listeDesWaysContenantLeNoeud.getListeDesWays();
    }

    public void addWayToListeDesWaysContenantLeNoeud(Way way){
        this.listeDesWaysContenantLeNoeud.addWay(way);
    }

    public void addPositionDuNoeudDansLaWay(int position){
        this.listeDesWaysContenantLeNoeud.addPosition(position);
    }

    public List<Integer> getListeDesPositions(){
        return this.listeDesWaysContenantLeNoeud.getPositions();
    }
}
