package jp.kobespiral.horie.todo.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jp.kobespiral.horie.todo.entity.ToDo;
import lombok.Data;

@Data
public class ToDoForm {
    @NotBlank
    @Size(max = 64)
    public String title; // ToDoのタイトル

    public ToDo toEntity(String mid) {
        long miliseconds = System.currentTimeMillis();
        Date date = new Date(miliseconds);
        ToDo todo = new ToDo(null, this.title, mid, false, date, null);
        return todo;
    }
}