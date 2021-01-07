package kawa.ttehaute.Graph;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kawa.ttehaute.XMLParser.Noeud;
import kawa.ttehaute.XMLParser.Way;

/**
 * Created by NextU on 16/02/2018.
 */

public class AutomobileGraph extends Graph {

    private TreeMap<Integer,GraphNode> arbreDeCorrespondance = null;
    private List<GraphNode> listeDesNoeudsDuGraphe = null;
    private int currentGraphNodeIndex = 0;
    private int currentArcIndex = 0;

    public AutomobileGraph(){
        this.arbreDeCorrespondance = new TreeMap<Integer,GraphNode>();
        this.listeDesNoeudsDuGraphe = new ArrayList<GraphNode>();
    }

    public void populateGraph(List<Way> ways){
        morphNoeudFromWaysToGraphNode(ways);
        constructGraphFromWays(ways);
    }

    private void morphNoeudFromWaysToGraphNode(List<Way> ways){
        if(ways!=null){
            int taille_de_la_liste_des_ways = ways.size();
            for(int index_of_way=0; index_of_way<taille_de_la_liste_des_ways; index_of_way++){
                Way current_way = ways.get(index_of_way);
                if(current_way!=null && current_way.isAHighway){
                    /*Map<String,String> liste_des_tags = current_way.getListeDeTag();
                    if(liste_des_tags!=null){
                        String highway_value = liste_des_tags.get("highway");
                        if(highway_value!=null){
                            populateGraphNodeList(current_way);
                        }
                    }*/
                    super.populateGraphNodeList(current_way);
                }
            }
        }
    }

    /*private void populateGraphNodeList(Way way){
        if(way!=null) {
            List<Noeud> liste_des_noeuds_de_la_way = way.getListeDeNoeud();
            int taille_de_la_liste = liste_des_noeuds_de_la_way.size();
            for(int index=0; index<taille_de_la_liste;index++){
                Noeud noeud = liste_des_noeuds_de_la_way.get(index);
                if(noeud!=null){
                    GraphNode graph_node = this.arbreDeCorrespondance.get(noeud.getId());
                    if(graph_node==null){
                        graph_node = new GraphNode(this.currentGraphNodeIndex++);
                        this.listeDesNoeudsDuGraphe.add(graph_node);
                        this.arbreDeCorrespondance.put(noeud.getId(),graph_node);
                        super.listeDesNoeuds.add(new ArrayList<Arc>());
                    }else{
                        // graph_node commun à plusieurs ways
                    }
                }
            }
        }
    }*/

    private void constructGraphFromWays(List<Way> ways){
        if(ways!=null){
            int taille_liste_ways = ways.size();
            for(int index_dans_ways=0; index_dans_ways<taille_liste_ways; index_dans_ways++){
                Way current_way = ways.get(index_dans_ways);
                if(current_way!=null){
                    if(current_way.isAHighway){
                        /*List<Noeud> listeDesNoeuds = current_way.getListeDeNoeud();
                        if(listeDesNoeuds!=null){
                            int taille_liste_noeuds = listeDesNoeuds.size();
                            for(int index_dans_noeuds=0; index_dans_noeuds<taille_liste_noeuds-1; index_dans_noeuds++){
                                Noeud current_noeud = listeDesNoeuds.get(index_dans_noeuds);
                                Noeud next_current_noeud = listeDesNoeuds.get(index_dans_noeuds+1);

                                if(current_noeud!=null && next_current_noeud!=null){
                                    GraphNode current_graph_node = this.arbreDeCorrespondance.get(current_noeud.getId());
                                    if(current_graph_node.getId()==125){
                                        Log.i("125=",""+current_noeud.getId());
                                    }
                                    GraphNode next_current_graph_node = this.arbreDeCorrespondance.get(next_current_noeud.getId());
                                    if(current_graph_node!=null && next_current_graph_node!=null){
                                        // récupère la liste des points associés à current_graph :
                                        List<Arc> arcs_from_current = super.listeDesNoeuds.get(current_graph_node.getId());
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
                                        }

                                        // et la meme chose pour next current graph mais reverse
                                        List<Arc> arcs_from_next_current = super.listeDesNoeuds.get(next_current_graph_node.getId());
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
                                        }
                                    }
                                }
                            }
                        }*/
                        super.addWayToGraph(current_way);
                    }
                }
            }
        }
    }



}
