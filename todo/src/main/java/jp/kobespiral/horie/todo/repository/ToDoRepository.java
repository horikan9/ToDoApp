package jp.kobespiral.horie.todo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.kobespiral.horie.todo.entity.ToDo;

@Repository
public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    List<ToDo> findToDoByMidAndDone(String mid, boolean done);

    List<ToDo> findToDoByDone(boolean done);

}
