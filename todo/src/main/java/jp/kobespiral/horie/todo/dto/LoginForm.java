package jp.kobespiral.horie.todo.dto;

import javax.validation.constraints.NotBlank;

import jp.kobespiral.horie.todo.entity.Member;
import lombok.Data;

@Data
public class LoginForm {
    @NotBlank
    String mid; // メンバーID．
}