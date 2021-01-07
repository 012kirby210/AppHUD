package kawa.ttehaute.XMLParser;

/**
 * Created by NextU on 16/02/2018.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to store list of ways from which a particular node is from
 * and the position of the node in each way
 */

public class WayWrapper {

    private List<Way> listeDesWays = null;
    private List<Integer> positions = null;

    public WayWrapper(){
        this.listeDesWays = new ArrayList<Way>();
        this.positions = new ArrayList<Integer>();
    }

    public void addWay(Way way){
        this.listeDesWays.add(way);
    }

    public List<Way> getListeDesWays(){
        return this.listeDesWays;
    }

    public void addPosition(int p){
        this.positions.add(p);
    }

    public List<Integer> getPositions(){
        return this.positions;
    }

}
