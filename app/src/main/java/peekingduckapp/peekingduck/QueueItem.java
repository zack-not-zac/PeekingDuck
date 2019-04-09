package peekingduckapp.peekingduck;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "queue_table")
public class QueueItem {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String script_name;
    private String script_body;
    private int pos;

    public QueueItem(String script_name, String script_body, int pos) {
        this.script_name = script_name;
        this.script_body = script_body;
        this.pos = pos;
    }

    public String getScript_name() {
        return script_name;
    }

    public void setScript_name(String script_name) {
        this.script_name = script_name;
    }

    public String getScript_body() {
        return script_body;
    }

    public void setScript_body(String script_body) {
        this.script_body = script_body;
    }


    public int getID() {
        return ID;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
