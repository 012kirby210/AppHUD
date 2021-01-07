package kawa.ttehaute.XMLParser;

/**
 * Created by NextU on 16/02/2018.
 */

public class Member {

    private String type;
    private long id;
    private String role;

    public Member(long id,String type){
        this.id = id;
        this.type = type;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getRole(){
        return this.role;
    }

    public void setRole(String role){
        this.role=role;
    }

}
