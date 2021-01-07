package kawa.ttehaute.XMLParser;

/**
 * Created by NextU on 17/02/2018.
 */

public class ConvertisseurDeVitesse {

    public final static double VITESSE_MOTORWAY = 130;  // autoroutes
    public final static double VITESSE_TRUNK = 110;     // Nationnales séparées par terre-plein
    public final static double VITESSE_PRIMARY = 90;
    public final static double VITESSE_SECONDARY = 90;
    public final static double VITESSE_TERTIARY = 90;
    public final static double VITESSE_MOTORWAY_LINK = 50;
    public final static double VITESSE_TRUNK_LINK = 50;
    public final static double VITESSE_PRIMARY_LINK = 50;
    public final static double VITESSE_SECONDARY_LINK = 50;
    public final static double VITESSE_TERTIARY_LINK = 50;
    public final static double VITESSE_ROAD = 90;
    public final static double VITESSE_UNCLASSIFIED = 90;
    public final static double VITESSE_RESIDENTIAL = 30;
    public final static double VITESSE_LIVING_STREET = 20;
    public final static double VITESSE_SERVICE = 5;

    private String countryCode;

    public ConvertisseurDeVitesse(String countryCode){
        this.countryCode = countryCode;
    }

}
