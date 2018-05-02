package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import org.springframework.data.annotation.Id;

public class Heroes  {

    @Id
    @Column("char_id")
    private int charId;
    @Column("char_name")
    private String charName;
    @Column("class_id")
    private int classId;
    private int count;
    private int played;

    public Heroes() {}

    public Heroes(Integer heroId, String name, int classId, int count, int played) {
        this.charId = heroId;
        this.charName = name;
        this.classId = classId;
        this.count = count;
        this.played = played;
    }

    public int getId() {
        return charId;
    }

    public String getCharName() {
        return charName;
    }

    public int getClassId() {
        return classId;
    }

    public int getCount() {
        return count;
    }

    public int getPlayed() {
        return played;
    }
}
