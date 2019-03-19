package peekingduckapp.peekingduck;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Script.class},version = 1)   //represents the Room database
public abstract class ScriptsDatabase extends RoomDatabase
{
    private static ScriptsDatabase instance;
    public abstract ScriptsDao ScriptsDao();

    static synchronized ScriptsDatabase getInstance(Context context) {
        if (instance == null)
        {
            //if no instance of the database exists, this statement will create it.
            //DestructiveMigration means that if the database version was changed, the app will destroy and rebuild the database.
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ScriptsDatabase.class, "note_database").fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulatedbAsyncTask(instance).execute();        //This will populate the database when it is first initialised
        }
    };

    private static class PopulatedbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private ScriptsDao scriptsDao;

        private PopulatedbAsyncTask(ScriptsDatabase database)
        {
            scriptsDao = database.ScriptsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {      //Default db values
            scriptsDao.insert(new Script("Title 1", "Path 1"));
            scriptsDao.insert(new Script("Title 2", "Path 2"));
            return null;
        }
    }

}
