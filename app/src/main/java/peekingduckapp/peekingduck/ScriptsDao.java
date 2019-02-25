package peekingduckapp.peekingduck;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao    //this is used to access data in the database (See: https://developer.android.com/training/data-storage/room/accessing-data)
public interface ScriptsDao
{
    @Insert
    void addScript(Scripts newScript);

    @Query("select * from scripts")
    List<Scripts> getScripts();

    @Delete
    void deleteScript(Scripts scripts);
}
