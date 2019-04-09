package peekingduckapp.peekingduck;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Script.class,QueueItem.class},version = 6, exportSchema = false)   //represents the Room database
public abstract class ScriptDatabase extends RoomDatabase
{
    private static ScriptDatabase instance;
    public abstract ScriptDao ScriptsDao();
    public abstract QueueDao queueDao();

    static synchronized ScriptDatabase getInstance(Context context) {
        if (instance == null)
        {
            //if no instance of the database exists, this statement will create it.
            //DestructiveMigration means that if the database version was changed, the app will destroy and rebuild the database.
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ScriptDatabase.class, "script_database").fallbackToDestructiveMigration()
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
        private ScriptDao scriptDao;
        private QueueDao queueDao;

        private PopulatedbAsyncTask(ScriptDatabase database)
        {
            scriptDao = database.ScriptsDao();
            queueDao = database.queueDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {      //Default db values
            scriptDao.insert(new Script("Title 1", "Path 1"));
            scriptDao.insert(new Script("Title 2", "Path 2"));

            queueDao.addToQueue(new QueueItem("Test Script 1",0));
            queueDao.addToQueue(new QueueItem("Test Script 2",1));
            queueDao.addToQueue(new QueueItem("Test Script 3",2));
            queueDao.addToQueue(new QueueItem("Test Script 4",3));
            return null;
        }
    }

}
