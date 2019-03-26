package peekingduckapp.peekingduck;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


//This repository is used to handle the multi-threading of the db tasks
public class ScriptRepo {
    private ScriptDao scriptDao;
    private QueueDao queueDao;
    private LiveData<List<Script>> scripts;
    private LiveData<List<QueueItem>> queue;

    public ScriptRepo (Application app)
    {
        ScriptDatabase database = ScriptDatabase.getInstance(app);
        scriptDao = database.ScriptsDao();
        scripts = scriptDao.viewScripts();

        queueDao = database.queueDao();
        queue = queueDao.viewQueue();
    }

    //These are the db functions visible to the rest of the app
    public void insert (Script script)
    {
        new InsertScriptAsyncTask(scriptDao).execute(script);
    }

    public void delete (Script script)
    {
        new DeleteScriptAsyncTask(scriptDao).execute(script);
    }

    public LiveData<List<Script>> getScripts() {
        return scripts;
    }



    public void addToQueue(QueueItem item)
    {
        new AddToQueueAsyncTask(queueDao).execute(item);
    }

    public void removeFromQueue(QueueItem item)
    {
        new RemoveFromQueueAsyncTask(queueDao).execute(item);
    }
    public LiveData<List<QueueItem>> viewQueue()
    {
        return queue;
    }



    //ASynchronous tasks must be used to use multi-threading and create a mutex so that multiple threads don't update the table at once
    private static class InsertScriptAsyncTask extends AsyncTask<Script, Void, Void>
    {
        private ScriptDao scriptDao;

        private InsertScriptAsyncTask (ScriptDao scriptDao){this.scriptDao = scriptDao;}

        @Override
        protected Void doInBackground(Script... scripts) {
            scriptDao.insert(scripts[0]);
            return null;
        }
    }

    private static class DeleteScriptAsyncTask extends AsyncTask<Script, Void, Void>
    {
        private ScriptDao scriptDao;

        private DeleteScriptAsyncTask (ScriptDao scriptDao){this.scriptDao = scriptDao;}

        @Override
        protected Void doInBackground(Script... scripts) {
            scriptDao.delete(scripts[0]);
            return null;
        }
    }



    private static class AddToQueueAsyncTask extends AsyncTask<QueueItem, Void, Void>
    {
        private QueueDao queueDao;

        private AddToQueueAsyncTask (QueueDao queueDao){this.queueDao = queueDao;}

        @Override
        protected Void doInBackground(QueueItem... queueItem) {
            queueDao.addToQueue(queueItem[0]);
            return null;
        }
    }
    private static class RemoveFromQueueAsyncTask extends AsyncTask<QueueItem, Void, Void>
    {
        private QueueDao queueDao;

        private RemoveFromQueueAsyncTask (QueueDao queueDao){this.queueDao = queueDao;}

        @Override
        protected Void doInBackground(QueueItem... queueItem) {
            queueDao.removeFromQueue(queueItem[0]);
            return null;
        }
    }
}
