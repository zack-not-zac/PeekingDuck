package peekingduckapp.peekingduck;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Queue;

public class ScriptViewModel extends AndroidViewModel {
    private ScriptRepo repo;
    private LiveData<List<Script>> scripts;
    private LiveData<List<QueueItem>> queue;

    public ScriptViewModel(@NonNull Application application) {
        super(application);

        repo = new ScriptRepo(application);
        scripts = repo.getScripts();
        queue = repo.viewQueue();
    }

    public void insert(Script script)
    {
        repo.insert(script);
    }

    public void delete(Script script)
    {
        repo.delete(script);
    }

    public LiveData<List<Script>> getAllScripts() {
        return scripts;
    }


    public void addToQueue(QueueItem item)
    {
        repo.addToQueue(item);
    }

    public void editQueueItem(QueueItem item)
    {
        repo.editQueueItem(item);
    }

    public void removeFromQueue(QueueItem item)
    {
        repo.removeFromQueue(item);
    }

    public LiveData<List<QueueItem>> viewQueue()
    {
        return queue;
    }
}
