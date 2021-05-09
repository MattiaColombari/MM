package localDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM question")
    List<Question> getAll();

    @Query("SELECT * FROM question WHERE qid IN (:qIds)")
    List<Question> loadAllByIds(int[] qIds);

    @Query("SELECT * FROM question WHERE questionText IN (:values)")
    List<Question> loadAllByValue(int[] values);

    @Insert
    void insertAll(Question... question);

    @Query("DELETE FROM question")
    void deleteAll();

    @Delete
    void delete(Question... question);


}
