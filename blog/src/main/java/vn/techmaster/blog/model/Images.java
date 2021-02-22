package vn.techmaster.blog.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String proPicPath;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bug bug;

    @Transient
    public String getImage(){
        if(proPicPath == null){
            return null;
        }
        return "/bugRequest/"+proPicPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProPicPath() {
        return proPicPath;
    }

    public void setProPicPath(String proPicPath) {
        this.proPicPath = proPicPath;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
