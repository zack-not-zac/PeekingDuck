package peekingduckapp.peekingduck;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity (tableName = "scripts")
public class Scripts {
    //variables
    @PrimaryKey
    @NonNull
    private int script_id;
    private String script_name;
    private String script_path; //TODO: Might need removed, don't know if this var will be necessary in final application

    public int getScript_id() {
        return script_id;
    }

    public void setScript_id(int script_id) {
        this.script_id = script_id;
    }

    public String getScript_name() {
        return script_name;
    }

    public void setScript_name(String script_name) {
        this.script_name = script_name;
    }

    public String getScript_path() {
        return script_path;
    }

    public void setScript_path(String script_path) {
        this.script_path = script_path;
    }
}
