package peekingduckapp.peekingduck;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface QueueDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void addToQueue(QueueItem item);

    @Update
    void editQueueItem(QueueItem item);

    @Delete
    void removeFromQueue(QueueItem item);

    @Query("DELETE FROM queue_table")
    void deleteAllQueue();

    @Query("SELECT count(ID) FROM queue_table")
    LiveData<Integer> countQueueItems();

    @Query("SELECT * FROM queue_table ORDER BY pos ASC")
    LiveData<List<QueueItem>> viewQueue();

    @Query("UPDATE queue_table SET script_body = :body WHERE ID = :id")
    int updateScriptBody(String body, int id);
}
