package com.example.demo.entity;

import com.example.demo.utils.VNCharacterUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
public class Post implements Comparable<Post>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDateTime lastUpdate;
    @PrePersist //Trước khi lưu khi khởi tạo record
    public void prePersist() {
        lastUpdate = LocalDateTime.now();
        slug= VNCharacterUtils.removeAccent(title);
        slug=slug.replaceAll(" ","-");
        slug=slug.replaceAll("\"","");
    }
    @PreUpdate //Khi cập nhật record
    public void preUpdate() {
        lastUpdate = LocalDateTime.now();
        slug= VNCharacterUtils.removeAccent(title);
        slug=slug.replaceAll(" ","-");
        slug=slug.replaceAll("\"","");
    }

    @Column(length = 100)
    private String title;

    public Post(LocalDateTime lastUpdate, String title, String content, String thumbnail) {
        this.lastUpdate = lastUpdate;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
    }

    @Column(length = 5000)
    private String content;

    private String thumbnail;
    private String user;
    private String slug;


    @Override
    public int compareTo(Post o) {
        if(this.lastUpdate.isAfter(o.lastUpdate)){
            return -1;
        }
        if(this.lastUpdate.isBefore(o.lastUpdate)){
            return 1;
        }
        return 0;
    }
}
