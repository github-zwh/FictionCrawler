package com.zwh.FictionCrawler.Pojo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int fic_id;
    private String Chapter_name;
    private String Chapter_text;
    private Date created;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFic_id() {
        return fic_id;
    }

    public void setFic_id(int fic_id) {
        this.fic_id = fic_id;
    }

    public String getChapter_name() {
        return Chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        Chapter_name = chapter_name;
    }

    public String getChapter_text() {
        return Chapter_text;
    }

    public void setChapter_text(String chapter_text) {
        Chapter_text = chapter_text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
