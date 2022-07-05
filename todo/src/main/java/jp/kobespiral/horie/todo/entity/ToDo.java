package jp.kobespiral.horie.todo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long seq; // 通し番号
    String title; // 題目
    String mid; // 作成者
    boolean done; // 完了フラグ
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt; // 作成日時
    @Temporal(TemporalType.TIMESTAMP)
    Date doneAt; // 完了日時

    public ToDo done() {
        this.done = true;
        Date date = new Date();
        this.doneAt = date;
        return this;
    }

    public ToDo update(String t) {
        this.title = t;
        Date date = new Date();
        this.createdAt = date;
        return this;
    }
}
