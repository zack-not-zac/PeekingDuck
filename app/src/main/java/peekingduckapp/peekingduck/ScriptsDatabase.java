package peekingduckapp.peekingduck;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Scripts.class},version = 1)   //represents the Room database
public abstract class ScriptsDatabase extends RoomDatabase
{
    public abstract ScriptsDao ScriptsDao();
}
