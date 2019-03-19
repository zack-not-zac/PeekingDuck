package peekingduckapp.peekingduck;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


//This repository is used to handle the multi-threading of the db tasks
public class ScriptRepo {
    private ScriptsDao scriptsDao;
    private LiveData<List<Script>> scripts;

    public ScriptRepo (Application app)
    {
        ScriptsDatabase database = ScriptsDatabase.getInstance(app);
        scriptsDao = database.ScriptsDao();
        scripts = scriptsDao.viewScripts();
    }

    //These are the db functions visible to the rest of the app
    public void insert (Script script)
    {
        new InsertScriptAsyncTask(scriptsDao).execute(script);
    }

    public void delete (Script script)
    {
        new DeleteScriptAsyncTask(scriptsDao).execute(script);
    }

    public LiveData<List<Script>> getScripts() {
        return scripts;
    }

    //ASynchronous tasks must be used to use multi-threading and create a mutex so that multiple threads don't update the table at once
    private static class InsertScriptAsyncTask extends AsyncTask<Script, Void, Void>
    {
        private ScriptsDao scriptsDao;

        private InsertScriptAsyncTask (ScriptsDao scriptsDao){this.scriptsDao = scriptsDao;}

        @Override
        protected Void doInBackground(Script... scripts) {
            scriptsDao.insert(scripts[0]);
            return null;
        }
    }

    private static class DeleteScriptAsyncTask extends AsyncTask<Script, Void, Void>
    {
        private ScriptsDao scriptsDao;

        private DeleteScriptAsyncTask (ScriptsDao scriptsDao){this.scriptsDao = scriptsDao;}

        @Override
        protected Void doInBackground(Script... scripts) {
            scriptsDao.delete(scripts[0]);
            return null;
        }
    }
}
