package peekingduckapp.peekingduck;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao    //this is used to access data in the database (See: https://developer.android.com/training/data-storage/room/accessing-data)
public interface ScriptsDao
{
    @Insert
    public void addScript(Scripts newScript);
}
