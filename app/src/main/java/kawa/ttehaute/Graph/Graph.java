package kawa.ttehaute.Graph;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import kawa.ttehaute.XMLParser.Noeud;
import kawa.ttehaute.XMLParser.Way;

/**
 * Created by NextU on 16/02/2018.
 */

public class Graph {

    protected List<List<Arc>> listeDesNoeuds;
    private TreeMap<Long,GraphNode> arbreDeCorrespondance = null;
    private List<GraphNode> listeDesNoeudsDuGraphe = null;
    private int currentGraphNodeIndex = 0;
    private int currentArcIndex = 0;

    public Graph(){
        this.listeDesNoeuds = new ArrayList<List<Arc>>();
        this.arbreDeCorrespondance = new TreeMap<Long,GraphNode>();
        this.listeDesNoeudsDuGraphe = new ArrayList<GraphNode>();
    }

    /**
     * Transforme chaque noeud référent osm contenu dans la way passée en paramètre en un noeud référent graph
     *
     * @param way la way dont les noeuds sont à considérer
     */

    protected void populateGraphNodeList(Way way){
        if(way!=null) {
            List<Noeud> liste_des_noeuds_de_la_way = way.getListeDeNoeud();
            int taille_de_la_liste = liste_des_noeuds_de_la_way.size();
            for(int index=0; index<taille_de_la_liste;index++){
                Noeud noeud = liste_des_noeuds_de_la_way.get(index);
                if(noeud!=null){
                    GraphNode graph_node = this.arbreDeCorrespondance.get(noeud.getId());
                    if(graph_node==null){
                        if(noeud.getId()==1827744089){ Log.i("populate",""+noeud.getId()+" : "+this.currentGraphNodeIndex); }
                        if(noeud.getId()==3063975209L){ Log.i("populate",""+noeud.getId()+" : "+this.currentGraphNodeIndex); }
                        graph_node = new GraphNode(this.currentGraphNodeIndex++);
                        graph_node.setLatitude(noeud.getLatitude());
                        graph_node.setLongitude(noeud.getLongitude());

                        this.listeDesNoeudsDuGraphe.add(graph_node);
                        this.arbreDeCorrespondance.put(noeud.getId(),graph_node);
                        this.listeDesNoeuds.add(new ArrayList<Arc>());
                    }else{
                        // graph_node commun à plusieurs ways => déjà enregistré
                    }
                }
            }
        }
    }

    /**
     * Extrait les arcs de la way et les ajoute au graph
     * @param way la way dont les arcs sont à considérés
     */

    protected void addWayToGraph(Way way){
        List<Noeud> liste_des_noeuds_de_la_way = way.getListeDeNoeud();
        if(liste_des_noeuds_de_la_way!=null){
            int taille_liste_noeuds = liste_des_noeuds_de_la_way.size();
            for(int index_dans_noeuds=0; index_dans_noeuds<taille_liste_noeuds-1; index_dans_noeuds++){
                Noeud current_noeud = liste_des_noeuds_de_la_way.get(index_dans_noeuds);
                Noeud next_current_noeud = liste_des_noeuds_de_la_way.get(index_dans_noeuds+1);

                if(current_noeud!=null && next_current_noeud!=null){
                    GraphNode current_graph_node = this.arbreDeCorrespondance.get(current_noeud.getId());
                    if(current_graph_node.getId()==125){
                        Log.i("125=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==124){
                        Log.i("124=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==81){
                        Log.i("81=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==213){
                        Log.i("213=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==150){
                        Log.i("150=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==149){
                        Log.i("149=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==151){
                        Log.i("151=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==179){
                        Log.i("179=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==173){
                        Log.i("173=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==172){
                        Log.i("172=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==171){
                        Log.i("171=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==180){
                        Log.i("180=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==664){
                        Log.i("664=",""+current_noeud.getId());
                    }
                    if(current_graph_node.getId()==192){
                        Log.i("192=",""+current_noeud.getId());
                    }
                    GraphNode next_current_graph_node = this.arbreDeCorrespondance.get(next_current_noeud.getId());
                    if(current_graph_node!=null && next_current_graph_node!=null){
                        // récupère la liste des points associés à current_graph :
                        /*List<Arc> arcs_from_current = this.listeDesNoeuds.get(current_graph_node.getId());
                        if(arcs_from_current!=null){
                            int nombre_arcs_from_current = arcs_from_current.size();
                            boolean next_current_graph_node_already_contained=false;
                            for(int index_dans_arcs=0; index_dans_arcs<nombre_arcs_from_current; index_dans_arcs++){
                                next_current_graph_node_already_contained = arcs_from_current.get(index_dans_arcs).getTerminaisonGraphNode().equals(next_current_graph_node);
                                if(next_current_graph_node_already_contained){
                                    break;
                                }
                            }
                            if(!next_current_graph_node_already_contained){
                                Arc arc_from_current_to_next_current = new Arc(this.currentArcIndex++,next_current_graph_node);
                                // en fonction de la valeur de la clef highway : déterminer la vitesse et la distance
                                // arc_from_current_to_next_current.setDistance(), arc_from_current_to_next_current.setVitesse()
                                arcs_from_current.add(arc_from_current_to_next_current);
                            }
                        }*/

                        // et la meme chose pour next current graph mais reverse
                        /*List<Arc> arcs_from_next_current = this.listeDesNoeuds.get(next_current_graph_node.getId());
                        if(arcs_from_next_current!=null){
                            int nombre_arcs_from_next_current = arcs_from_next_current.size();
                            boolean current_graph_node_already_contained=false;
                            for(int index_dans_arcs=0; index_dans_arcs<nombre_arcs_from_next_current; index_dans_arcs++){
                                current_graph_node_already_contained = arcs_from_current.get(index_dans_arcs).getTerminaisonGraphNode().equals(next_current_graph_node);
                                if(current_graph_node_already_contained){
                                    break;
                                }
                            }
                            if(!current_graph_node_already_contained){
                                Arc arc_from_next_current_to_current = new Arc(this.currentArcIndex++,current_graph_node);
                                // en fonction de la valeur de la clef highway : déterminer la vitesse et la distance
                                // arc_from_current_to_next_current.setDistance(), arc_from_current_to_next_current.setVitesse()
                                arcs_from_next_current.add(arc_from_next_current_to_current);
                            }
                        }*/
                        addArcToGraph(current_graph_node,next_current_graph_node,way.vitesse);
                        addArcToGraph(next_current_graph_node,current_graph_node,way.vitesse);
                    }
                }
            }
        }
    }

    /**
     * Détermine l'arc formé par les deux GraphNode passé en paramètre, et procède à son adjonction dans le graphe si celui-ci
     * n'existe pas déjà.
     * @param current_node le noeud source
     * @param next_current_node le noeud arrivé
     * @param vitesse la vitesse associée à la way de circulation
     */

    private void addArcToGraph(GraphNode current_node, GraphNode next_current_node,double vitesse){
        List<Arc> arcs_from_current = this.listeDesNoeuds.get(current_node.getId());
        if(arcs_from_current!=null){
            int nombre_arcs_from_current = arcs_from_current.size();
            boolean next_current_graph_node_already_contained=false;
            for(int index_dans_arcs=0; index_dans_arcs<nombre_arcs_from_current && !next_current_graph_node_already_contained ; index_dans_arcs++){
                next_current_graph_node_already_contained = arcs_from_current.get(index_dans_arcs).getTerminaisonGraphNode().equals(next_current_node);
                /*if(next_current_graph_node_already_contained){
                    break;
                }*/
            }
            if(!next_current_graph_node_already_contained){
                Arc arc_from_current_to_next_current = new Arc(this.currentArcIndex++,next_current_node);
                arc_from_current_to_next_current.setVitesse(vitesse);
                // en fonction de la valeur de la clef highway : déterminer la vitesse et la distance
                // arc_from_current_to_next_current.setDistance(), arc_from_current_to_next_current.setVitesse()
                arcs_from_current.add(arc_from_current_to_next_current);

                setDistanceToArcLineaire(arc_from_current_to_next_current,current_node,next_current_node);
            }
        }
    }

    /**
     * Calcul la distance entre les deux points de l'arc et la définie dans celui-ci
     * @param arc l'arc considéré
     * @param first_node le premier noeud pour extraction des coordonnées
     * @param second_node le second noeud pour extraction des coordonnées
     */

    private void setDistanceToArc(Arc arc, GraphNode first_node, GraphNode second_node){
        double first_node_lat = Math.toRadians(first_node.getLatitude());
        double first_node_lon = Math.toRadians(first_node.getLongitude());
        double second_node_lat = Math.toRadians(second_node.getLatitude());
        double second_node_lon = Math.toRadians(second_node.getLongitude());

        double delta_lat = first_node_lat-second_node_lat;
        double delta_lon = first_node_lon-second_node_lon;

        // distance = 2xEarthRadiusx arcsin[ sqrt(sin²(delta_lat/2)+cos(first_node_lat)cos(second_node_lat)sin²(delta_lon/2)) ]
        // distance = 2xEarthRadiusx arcsin[ sqrt(_sin1² + factor x _sin2²) ]

        double _sin1 = Math.sin(delta_lat/2);
        double _sin2 = Math.sin(delta_lon/2);
        double factor = Math.cos(first_node_lat)*Math.cos(second_node_lat);

        // distance = 2 x EarthRadius x arcsin(sqrt(sum))

        double sum = _sin1*_sin1 + factor*_sin2*_sin2;
        double distance = Math.asin(Math.sqrt(sum));
        distance*=2*Arc.rayonTerrestre;

        arc.setDistance(distance);
    }

    /**
     * Calcul la distance entre les deux points de l'arc et la définie dans celui-ci
     * @param arc l'arc considéré
     * @param first_node le premier noeud pour extraction des coordonnées
     * @param second_node le second noeud pour extraction des coordonnées
     */

    private void setDistanceToArcLineaire(Arc arc, GraphNode first_node, GraphNode second_node){
        double first_node_lat = Math.toRadians(first_node.getLatitude());
        double first_node_lon = Math.toRadians(first_node.getLongitude());
        double second_node_lat = Math.toRadians(second_node.getLatitude());
        double second_node_lon = Math.toRadians(second_node.getLongitude());

        double delta_lat = first_node_lat-second_node_lat;
        double delta_lon = first_node_lon-second_node_lon;

        // distance = EarthRadius x sqrt( sin²(delta_lon) + sin²(delta_lat) )
        // distance = EarthRadius x sqrt( _sin1² + _sin2² )

        double _sin1 = Math.sin(delta_lon);
        double _sin2 = Math.sin(delta_lat);

        // distance = EarthRadius x sqrt(sum)

        double sum = _sin1*_sin1 + _sin2*_sin2;
        double distance = Math.sqrt(sum);
        distance*=Arc.rayonTerrestre;

        arc.setDistance(distance);
    }


    public String toString(){
        String return_string = "GraphNodes : "+this.listeDesNoeudsDuGraphe.size()+", nombre d'arcs : "+(this.currentArcIndex-1);
        return return_string;
    }

    public void test(){
        String incidence = "incidence du noeud : "+this.listeDesNoeuds.get(125).size();
        String construction = "";
        for(int i=0; i<this.listeDesNoeuds.get(125).size();i++){
            construction+="noeud initial : "+125+", noeud final : "+this.listeDesNoeuds.get(125).get(i).getTerminaisonGraphNode().getId()+" ; distance entre les deux : "+
                    this.listeDesNoeuds.get(125).get(i).getdistance()+"\n";
        }
        Log.i("test graph",incidence+" ; "+construction);

        incidence = "incidence du noeud : "+this.listeDesNoeuds.get(150).size();
        construction = "";
        for(int i=0; i<this.listeDesNoeuds.get(150).size();i++){
            construction+="noeud initial : "+150+", noeud final : "+this.listeDesNoeuds.get(150).get(i).getTerminaisonGraphNode().getId()+" ; distance entre les deux : "+
                    this.listeDesNoeuds.get(150).get(i).getdistance()+"\n";
        }
        Log.i("test graph",incidence+" ; "+construction);

        incidence = "incidence du noeud : "+this.listeDesNoeuds.get(180).size();
        construction = "";
        for(int i=0; i<this.listeDesNoeuds.get(180).size();i++){
            construction+="noeud initial : "+180+", noeud final : "+this.listeDesNoeuds.get(180).get(i).getTerminaisonGraphNode().getId()+" ; distance entre les deux : "+
                    this.listeDesNoeuds.get(180).get(i).getdistance()+"\n";
        }
        Log.i("test graph",incidence+" ; "+construction);

        incidence = "incidence du noeud : "+this.listeDesNoeuds.get(172).size();
        construction = "";
        for(int i=0; i<this.listeDesNoeuds.get(172).size();i++){
            construction+="noeud initial : "+172+", noeud final : "+this.listeDesNoeuds.get(172).get(i).getTerminaisonGraphNode().getId()+" ; distance entre les deux : "+
                    this.listeDesNoeuds.get(172).get(i).getdistance()+"\n";
        }
        Log.i("test graph",incidence+" ; "+construction);
    }
}
