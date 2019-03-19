package peekingduckapp.peekingduck;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ScriptViewModel extends AndroidViewModel {
    private ScriptRepo repo;
    private LiveData<List<Script>> scripts;

    public ScriptViewModel(@NonNull Application application) {
        super(application);

        repo = new ScriptRepo(application);
        scripts = repo.getScripts();
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
}
