package peekingduckapp.peekingduck;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity (tableName = "queue_table")
public class QueueItem {
    @PrimaryKey (autoGenerate = true)
    private int ID;

    private String script_body;

    public QueueItem(String script_body)
    {
        this.script_body = script_body;
    }

    public void setScript_body(String script_body)
    {
        this.script_body = script_body;
    }

    public String getScript_body()
    {
        return script_body;
    }

   public int getID()
   {
       return ID;
   }

   public void setID(int ID)
   {
       this.ID = ID;
   }
}
