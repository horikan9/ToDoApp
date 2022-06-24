package jp.kobespiral.horie.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.kobespiral.horie.todo.dto.ToDoForm;
import jp.kobespiral.horie.todo.entity.ToDo;
import jp.kobespiral.horie.todo.exception.ToDoAppException;
import jp.kobespiral.horie.todo.repository.MemberRepository;
import jp.kobespiral.horie.todo.repository.ToDoRepository;

@Service
public class ToDoService {
    @Autowired
    MemberRepository mRepo;
    @Autowired
    ToDoRepository todoRepo;

    public ToDo createToDo(String mid, ToDoForm form) {
        ToDo todo = form.toEntity(mid);
        return todoRepo.save(todo);
        /* メンバーmidが新しくToDoを作成する */}

    public ToDo updateToDo(Long seq) {
        ToDo todo = getToDo(seq);
        ToDo newtodo = todo.update();
        return todoRepo.save(newtodo);
        /* メンバーmidが新しくToDoを作成する */}

    public ToDo getToDo(Long seq) {
        ToDo todo = todoRepo.findById(seq).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_MEMBER_EXISTS, seq + ": No such member exists"));
        return todo;
        /* 番号を指定してToDoを取得 */}

    public List<ToDo> getToDoList(String mid) {
        List<ToDo> list = todoRepo.findToDoByMidAndDone(mid, false);
        return list;
        /* midのToDoリストを取得 */}

    public List<ToDo> getDoneList(String mid) {
        List<ToDo> list = todoRepo.findToDoByMidAndDone(mid, true);
        return list;
        /* midのDoneリストを取得 */}

    public List<ToDo> getToDoList() {
        List<ToDo> list = todoRepo.findToDoByDone(false);
        return list;
        /* 全員のToDoリストを取得 */}

    public List<ToDo> getDoneList() {
        List<ToDo> list = todoRepo.findToDoByDone(true);
        return list;
        /* 全員のDoneリストを取得 */}

}
