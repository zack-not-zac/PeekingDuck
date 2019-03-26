package peekingduckapp.peekingduck;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface QueueDao {
    @Insert
    void addToQueue(QueueItem item);

    @Delete
    void removeFromQueue(QueueItem item);

    @Query("SELECT * FROM queue_table")
    LiveData<List<QueueItem>> viewQueue();
}
