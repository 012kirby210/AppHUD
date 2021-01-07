package kawa.ttehaute.Graph;

/**
 * Created by NextU on 16/02/2018.
 */

public class GraphNode {
    private int id;
    private double latitude;
    private double longitude;
    private double elevation;

    public GraphNode(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLongitude(double distance){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setElevation(double elevation){
        this.elevation = elevation;
    }

    public double getElevation(){
        return this.elevation;
    }
}
