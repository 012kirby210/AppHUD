package kawa.ttehaute;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NextU on 11/02/2018.
 */

public class Gpx {

    private String nomDuFichierTrack;
    private String nomDuFichierWaypoint;
    private File fileToWriteTrackInto = null;
    private File fileToWriteWaypointsInto = null;
    private static final String GPX_TAG_TRACK_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+"<gpx " +
            "xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"teteHauteDisplay\" "+
            " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "+
            " xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n " +
            "<trk>\n" +
            "<trkseg>\n";
    private static final String GPX_TAG_TRACK_END = "</trkseg>\n" +
            "</trk>\n" +
            "</gpx>\n";
    private static final String GPX_TAG_WAYPOINT_START = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+"<gpx " +
            "xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\">\n";
    private static final String GPX_TAG_WAYPOINT_END = "</gpx>";
    private int count = 0;

    //private Activity activity

    public Gpx(String nomdufichier,String nomDuFichierWaypoint,Activity activity){
        this.nomDuFichierTrack = nomdufichier;
        this.nomDuFichierWaypoint = nomDuFichierWaypoint;
        File path = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        this.fileToWriteTrackInto = new File(path,this.nomDuFichierTrack);
        this.fileToWriteWaypointsInto = new File(path,this.nomDuFichierWaypoint);
        //this.activity = activity;
        if(this.fileToWriteTrackInto !=null){
            if(!this.fileToWriteTrackInto.exists()){
                try{
                    this.fileToWriteTrackInto.createNewFile();
                }catch(IOException e){
                    // filetoWriteinto stay null
                }
            }
        }
        if(this.fileToWriteWaypointsInto !=null){
            if(!this.fileToWriteWaypointsInto.exists()){
                try{
                    this.fileToWriteWaypointsInto.createNewFile();
                }catch(IOException e){
                    // filetoWriteinto stay null
                }
            }
        }
    }

    public void startGpxFile(){
        FileOutputStream trackFileOutputstream = null;
        FileOutputStream  waypointFileOutputStream = null;
        boolean externalStorageWritable = isExternalStorageWritable();
        if(fileToWriteTrackInto !=null && fileToWriteWaypointsInto !=null && externalStorageWritable){
            try{
                trackFileOutputstream = new FileOutputStream(this.fileToWriteTrackInto,true);
                waypointFileOutputStream = new FileOutputStream(this.fileToWriteWaypointsInto, true);
                trackFileOutputstream.write(GPX_TAG_TRACK_START.getBytes());
                waypointFileOutputStream.write(GPX_TAG_WAYPOINT_START.getBytes());
            }catch(FileNotFoundException e){

            }catch(IOException e){

            }finally{
                try{
                    if(trackFileOutputstream != null){
                        trackFileOutputstream.close();
                    }
                    if(waypointFileOutputStream != null ){
                        waypointFileOutputStream.close();
                    }
                }catch(IOException e){

                }
            }
        }
    }

    public void stopGpxFile(){
        FileOutputStream trackFileOutputStream = null;
        FileOutputStream waypointFileOutputStream = null;
        boolean externalStorageWritable = isExternalStorageWritable();
        if(fileToWriteTrackInto !=null && fileToWriteWaypointsInto !=null && externalStorageWritable){
            try{
                trackFileOutputStream = new FileOutputStream(this.fileToWriteTrackInto,true);
                waypointFileOutputStream = new FileOutputStream(this.fileToWriteWaypointsInto, true);
                trackFileOutputStream.write(this.GPX_TAG_TRACK_END.getBytes());
                waypointFileOutputStream.write(this.GPX_TAG_WAYPOINT_END.getBytes());
            }catch(FileNotFoundException e){
                //Log.i("stop","file not found");
            }catch(IOException e){
                //Log.i("stop","io exception");
            }finally{
                try{
                    if(trackFileOutputStream != null){
                        trackFileOutputStream.close();
                    }
                    if(waypointFileOutputStream != null){
                        waypointFileOutputStream.close();
                    }
                }catch(IOException e){

                }
            }
        }else{
            //Log.i("stop","file null or externalstorage unavailable");
        }
    }

    public void recordTrkPoint(double latitude, double longitude, double elevation,long time){
        FileOutputStream fileoutputstream=null;
        boolean externalStorageWritable = isExternalStorageWritable();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        if(fileToWriteTrackInto !=null && externalStorageWritable){
            try{
                fileoutputstream = new FileOutputStream(this.fileToWriteTrackInto,true);
                //Date dateNow = new Date();
                String trkpt_tag = "<trkpt lat=\""+Double.toString(latitude)+"\" lon=\""+Double.toString(longitude)+"\">\n";
                String ele_tag = "<ele>"+Double.toString(elevation)+"</ele>\n";
                String time_tag = "<time>"+dateformat.format(new Date(time))+"</time>\n";
                String trkpt_end_tag = "</trkpt>\n";
                String string_to_write = trkpt_tag+ele_tag+time_tag+trkpt_end_tag;
                //Log.i("trak",dateformat.format(dateNow).toString());
                fileoutputstream.write(string_to_write.getBytes());

            }catch(FileNotFoundException e){

            }catch(IOException e){

            }finally{
                try{
                    if(fileoutputstream!=null){
                        fileoutputstream.close();
                    }
                }catch(IOException e){

                }
            }
        }
    }

    public void recordWayPoint(double latitude, double longitude, double elevation,long time) {
        FileOutputStream fileoutputstream = null;
        boolean externalStorageWritable = isExternalStorageWritable();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        if (fileToWriteWaypointsInto != null && externalStorageWritable) {
            try {
                fileoutputstream = new FileOutputStream(this.fileToWriteWaypointsInto, true);
                String wpt_tag = "<wpt lat=\"" + Double.toString(latitude) + "\" lon=\"" + Double.toString(longitude) + "\">\n";
                String ele_tag = "<ele>" + Double.toString(elevation) + "</ele>\n";
                String time_tag = "<time>" + dateformat.format(new Date(time)) + "</time>\n";
                String name_tag = "<name>Waypoint"+count+"</name>";
                count+=1;
                String wpt_end_tag = "</wpt>\n";
                String string_to_write = wpt_tag + ele_tag + time_tag + name_tag + wpt_end_tag;
                //Log.i("trak",dateformat.format(dateNow).toString());
                fileoutputstream.write(string_to_write.getBytes());

            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            } finally {
                try {
                    if (fileoutputstream != null) {
                        fileoutputstream.close();
                    }
                } catch (IOException e) {

                }
            }
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
