package peekingduckapp.peekingduck;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao    //this is used to access data in the database (See: https://developer.android.com/training/data-storage/room/accessing-data)
public interface ScriptDao
{
    @Insert
    void insert(Script newScript);

    @Delete
    void delete(Script script);

    @Query("SELECT * FROM scripts")
    LiveData<List<Script>> viewScripts();
}
