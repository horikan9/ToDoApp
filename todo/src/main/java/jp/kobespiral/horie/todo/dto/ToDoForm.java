package jp.kobespiral.horie.todo.dto;

import java.util.Date;

import jp.kobespiral.horie.todo.entity.ToDo;
import lombok.Data;

@Data
public class ToDoForm {
    public String title; // ToDoのタイトル

    public ToDo toEntity(String mid) {
        long miliseconds = System.currentTimeMillis();
        Date date = new Date(miliseconds);
        ToDo todo = new ToDo(null, this.title, mid, false, date, null);
        return todo;
    }
}