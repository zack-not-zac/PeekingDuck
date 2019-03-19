package peekingduckapp.peekingduck;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity (tableName = "scripts")
public class Script {
    //variables
    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int script_id;

    private String script_name;
    private String script_path;

    public Script(String script_name, String script_path)
    {
        this.script_name = script_name;
        this.script_path = script_path;
    }

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
