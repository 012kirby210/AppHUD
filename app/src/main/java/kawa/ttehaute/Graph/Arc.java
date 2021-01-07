package kawa.ttehaute.Graph;

/**
 * Created by NextU on 16/02/2018.
 */

public class Arc {
    private int id;
    private double vitesse;
    private double distance;
    private GraphNode terminaisonGraphNode;
    public static final double rayonTerrestre = 6367444; // in meter

    public Arc(int id,GraphNode terminaisonGraphNode){
        this.id = id;
        this.terminaisonGraphNode = terminaisonGraphNode;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setVitesse(double vitesse){
        this.vitesse = vitesse;
    }

    public double getVitesse(){
        return this.vitesse;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    public double getdistance(){
        return this.distance;
    }

    public void setTerminaisonGraphNode(GraphNode terminaisonGraphNode){
        this.terminaisonGraphNode = terminaisonGraphNode ;
    }

    public GraphNode getTerminaisonGraphNode(){
        return this.terminaisonGraphNode;
    }
}
