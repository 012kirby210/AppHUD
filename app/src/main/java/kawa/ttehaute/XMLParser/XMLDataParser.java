package kawa.ttehaute.XMLParser;

import android.app.Activity;
import android.icu.text.MeasureFormat;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NextU on 16/02/2018.
 */

public class XMLDataParser {

    String nomDuFichierAAnalyser ;
    File fichierAAnalyser = null ;
    ArrayList<Noeud> listeDesNoeuds ;
    TreeMap<Long,Noeud> arbreDesNoeuds = null ;
    public ArrayList<Way> listeDeWays = null;
    TreeMap<Long,Way> arbreDesWays= null;
    ArrayList<Relation> listeDesRelations = null;
    TreeMap<Long,Relation> arbreDesRelations = null;
    Activity activityContext ;
    Way currentProcessedWay = null;
    Relation currentProcessedRelation = null;
    int lineCount = 0;
    int countNodePerWay = 0;


    //String nodeRegexp = "<node.*id=.(\\d*).*lat=.(\\d*\\.\\d*).*lon=.(\\d*\\.\\d*).*/>";

    /**
     * Crée un analyseur de fichier XML formatté OSM
     * @param nomFichier nom du fichier à analyser présent dans /Android/data/kawa.ttehaute/files/Documents/
     * @param activity activité dont provient le call
     */

    public XMLDataParser(String nomFichier, Activity activity){
        this.nomDuFichierAAnalyser = nomFichier;
        this.activityContext = activity;
        this.listeDesNoeuds = new ArrayList<Noeud>();
        this.arbreDesNoeuds = new TreeMap<Long,Noeud>();
        this.listeDeWays = new ArrayList<Way>();
        this.arbreDesWays = new TreeMap<Long,Way>();
        this.listeDesRelations = new ArrayList<Relation>();
        this.arbreDesRelations = new TreeMap<Long,Relation>();
    }

    /**
     * Vérifie si le fichier est bien présent
     * @return true, si le fichier est présent , false sinon.
     */

    private boolean openFile(){
        if( this.nomDuFichierAAnalyser != null && this.activityContext!=null ){
            File fileParentDir = activityContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ;
            this.fichierAAnalyser = new File(fileParentDir,nomDuFichierAAnalyser) ;
        }
        return this.fichierAAnalyser==null? false : true ;
    }

    /**
     * Ouvre et analyse le fichier dont le nom est passé au constructeur
     */

    public void parse(){
        // ouvrir le fichier à analyser
        FileReader reader = null ;
        BufferedReader buffer_reader = null ;
        if(openFile()){
            try{
                reader = new FileReader(this.fichierAAnalyser) ;
                buffer_reader = new BufferedReader(reader) ;
                String line = null ;
                boolean inside_a_way = false ;

                Way way = null;
                do{
                    try{
                        line = buffer_reader.readLine();
                        Noeud noeud = null;

                        if(line!=null){
                            parseLine(line);
                            this.lineCount++;
                        }
                    }catch (IOException e){

                    }catch(Exception e) {
                        // erreur format
                    }

                }while(line != null) ;

            }catch(FileNotFoundException e){

            }finally{
                try{
                    reader.close();
                }catch(IOException e){

                }
            }
        }
    }

    /**
     * Analyse la ligne en cours
     * @param line la ligne à analyser
     */

    private void parseLine(String line){
        try{
            if(!processNode(line)){
                // check if it's a way
                if(this.currentProcessedWay==null){
                    this.currentProcessedWay = processWay(line);
                }else{
                    // it is a way
                    if(itIsAWayCloseTag(line)){
                        this.arbreDesWays.put(this.currentProcessedWay.getId(),this.currentProcessedWay);
                        this.listeDeWays.add(this.currentProcessedWay);
                        this.currentProcessedWay = null;
                        this.countNodePerWay = 0;
                    }else{
                        // a way is composed of <nd></nd> and <tag></tag> tags
                        if(!processNd(line)){
                            processTag(line,this.currentProcessedWay);
                        }
                    }
                }

                // check if it's a relation
                if(this.currentProcessedRelation==null){
                    this.currentProcessedRelation = processRelation(line);
                }else{
                    // it is a relation
                    if(itIsARelationCloseTag(line)){
                        this.listeDesRelations.add(this.currentProcessedRelation);
                        this.arbreDesRelations.put(this.currentProcessedRelation.getId(),this.currentProcessedRelation);
                        this.currentProcessedRelation = null;
                    }else{
                        // a relation is composed of <member></member> and <tag></tag>
                        Member current_member = null;
                        current_member = processMember(line);
                        if(current_member!=null){
                            this.currentProcessedRelation.addMember(current_member);
                        }else{
                            // process tag then
                            processTag(line,this.currentProcessedRelation);
                        }
                    }

                }
            }
        }catch(NumberFormatException e){
            // Le fichier xml est mal formatté @ line lineCount
        }

    }

    /**
     * Vérifie si la ligne est un tag de cloture d'une way
     * @param line la ligne à analyser
     * @return true, si la ligne est un tag de cloture de way , false sinon
     */

    private boolean itIsAWayCloseTag(String line){
        boolean it_is = false ;
        String way_close_tag_regexp = ".*</way>.*";
        Pattern way_close_tag_pattern = Pattern.compile(way_close_tag_regexp);
        Matcher way_close_tag_matcher = way_close_tag_pattern.matcher(line);
        it_is = way_close_tag_matcher.matches();
        return it_is ;
    }

    /**
     *  détermine et analyse, cas échéant, si la ligne est un noeud
     * @param line la ligne à analyser
     * @return true si la ligne est une ligne de noeud, false sinon
     */
    private boolean processNode(String line) throws NumberFormatException{
        boolean was_a_node_line = false ;
        String node_regexp = ".*<node.*id=['\"](\\d*)['\"].*uid=['\"](\\d*)['\"].*lat=['\"](\\d*.\\d*)['\"].*lon=['\"](\\d*.\\d*)['\"].*" ;
        Pattern node_pattern = Pattern.compile(node_regexp);
        Matcher node_matcher = node_pattern.matcher(line);
        if(node_matcher.matches()){
            was_a_node_line = true ;
            String _id = node_matcher.group(1);
            String _lat = node_matcher.group(3);
            String _lon = node_matcher.group(4);
            if( _id!=null && _lat!=null && _lon!=null){
                long id = Long.parseLong(_id);
                double lat = Double.parseDouble(_lat);
                double lon = Double.parseDouble(_lon);
                Noeud noeud = new Noeud(lat,lon,id) ;
                this.listeDesNoeuds.add(noeud);
                this.arbreDesNoeuds.put(id,noeud);
            }
        }
        return was_a_node_line;
    }

    /**
     * détermine et analyse, cas échéant, si la ligne est une way
     * @param line la ligne à analyser
     * @return true si la ligne est une way, false sinon
     */
    private Way processWay(String line) throws NumberFormatException{
        Way way = null;

        String way_regexp = ".*<way.*id=.(\\d*).*>";
        Pattern way_pattern = Pattern.compile(way_regexp);
        Matcher way_matcher = way_pattern.matcher(line);
        if(way_matcher.matches()){
            String _id = way_matcher.group(1);
            if(_id!=null ){
                long way_id = Long.parseLong(_id);
                way = new Way(way_id);
            }
        }

        return way;
    }

    /**
     * Analyse la ligne tag et ajoute le tag dans l'object passé en paramètre
     * @param line la ligne à analyser
     * @param object Object dont la classe est soit Way, soit Relation afin d'ajouter le tag
     */
    private void processTag(String line, Object object){
        String tag_regexp = ".*<tag.*k=['\"]([^'\"]*)['\"].*v=['\"]([^'\"]*)['\"].*";
        Pattern tag_pattern = Pattern.compile(tag_regexp);
        Matcher tag_matcher = tag_pattern.matcher(line);
        if(tag_matcher.matches()){
            String key = tag_matcher.group(1);
            String value = tag_matcher.group(2);
            // to which obejct do we add the tag?
            if(key!=null && value!=null){
                if(object.getClass().getName().equals("kawa.ttehaute.XMLParser.Way")){
                    ((Way) object).addTag(key,value);
                }
                else if(object.getClass().getName().equals("kawa.ttehaute.XMLParser.Relation")){
                    ((Relation) object).addTag(key,value);
                }
            }
        }
    }

    /**
     * analyse la ligne nd, et ajoute la reference du noeud dans la liste des noeud de la way en cours
     * @param line la ligne à analyser
     * @return true, si la ligne est une ligne de nd , false sinon
     */
    private boolean processNd(String line) throws NumberFormatException{
        boolean is_a_nd_tag = false ;
        String nd_tag_regexp = ".*<nd.*ref=['\"]([^'\"]*)['\"].*";
        Pattern nd_tag_pattern = Pattern.compile(nd_tag_regexp);
        Matcher nd_tag_matcher = nd_tag_pattern.matcher(line);
        if(nd_tag_matcher.matches()){
            is_a_nd_tag = true ;

            // get the reference node :
            String _noeud_id = nd_tag_matcher.group(1);
            if(_noeud_id!=null){
                long noeud_id = Long.parseLong(_noeud_id);
                Noeud noeud = null ;
                noeud = this.arbreDesNoeuds.get(noeud_id);
                if(noeud!=null){
                    this.currentProcessedWay.addNoeud(noeud);
                    noeud.addWayToListeDesWaysContenantLeNoeud(this.currentProcessedWay);
                    noeud.addPositionDuNoeudDansLaWay(this.countNodePerWay);
                    this.countNodePerWay++;
                }
            }

        }
        return is_a_nd_tag;
    }

    /**
     * Détermine et analyse, cas échéant, si la ligne est une relation
     * @param line la ligne à analyser
     * @return  la relation en cours de traitement
     */

    private Relation processRelation(String line) throws NumberFormatException{
        Relation relation = null;
        String relation_regexp = ".*<relation.*id=['\"]([^'\"]*)['\"].*";
        Pattern relation_pattern = Pattern.compile(relation_regexp);
        Matcher relation_matcher = relation_pattern.matcher(line);
        if(relation_matcher.matches()){
            String _id = relation_matcher.group(1);
            if(_id!=null){
                long id = Long.parseLong(_id);
                relation = new Relation(id);
            }
        }
        return relation;
    }

    /**
     * Determine si la ligne est un tag de cloture de relation
     * @param line la ligne à analyser
     * @return true si le tag est un tag de cloture de relation, false sinon
     */

    private boolean itIsARelationCloseTag(String line){
        boolean it_is = false;
        String relation_close_tag_regexp = ".*</relation>.*";
        Pattern relation_close_tag_pattern = Pattern.compile(relation_close_tag_regexp);
        Matcher relation_close_tag_matcher = relation_close_tag_pattern.matcher(line);
        it_is = relation_close_tag_matcher.matches() ;

        return it_is;
    }

    /**
     * Determine et analyse cas si la ligne est un member tag, et l'ajoute à la relation en cours
     * @param line la ligne à analyser
     * @return le member ou null
     */

    private Member processMember(String line) throws NumberFormatException{
        Member member = null;
        String member_regexp = ".*<member.* type=['\"]([^'\"]*)['\"].*ref=['\"]([^'\"]*)['\"].*role=['\"]([^'\"]*)['\"].*/>";
        Pattern member_pattern = Pattern.compile(member_regexp) ;
        Matcher member_matcher = member_pattern.matcher(line);
        if(member_matcher.matches()){
            String _type = member_matcher.group(1);
            String _ref = member_matcher.group(2);
            String _role = member_matcher.group(3);
            if(_type!=null && _ref!=null ){
                long ref = Long.parseLong(_ref);
                member = new Member(ref,_type);
                if(_role!=null){
                   member.setRole(_role);
                }
            }
        }
        return member;
    }

    public String toString(){
        String string_parser = "Node count: "+this.listeDesNoeuds.size()+", Way Count: "+this.listeDeWays.size()+", Relation Count: "+this.listeDesRelations.size();
        return string_parser ;
    }

    public void test(){
        Relation rel = this.listeDesRelations.get(0) ;
        Set<String> tag_keys = rel.getListedesTags().keySet();

        for(int i=0; i<tag_keys.size(); i++){
            Log.i("tag",rel.getListedesTags().get(tag_keys.toArray()[i]));
        }

        int members_size = rel.getListeDesMembres().size();
        for(int i =0; i<members_size; i++){
            Log.i("members",rel.getListeDesMembres().get(i).getType());
        }

        Way way = this.listeDeWays.get(0);
        Log.i("test",""+way.getListeDeNoeud().get(0).getId());
        Noeud n = way.getListeDeNoeud().get(0);
        List<Way> ways = n.getListeDesWaysContenantLeNoeud();
        for(int i=0; i<ways.size(); i++){
            Log.i("test","noeud_id : "+n.getId()+", way : "+i+", position : "+n.getListeDesPositions().get(i));
        }

    }
}
