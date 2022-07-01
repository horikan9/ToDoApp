package jp.kobespiral.horie.todo.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.kobespiral.horie.todo.dto.LoginForm;
import jp.kobespiral.horie.todo.dto.ToDoForm;
import jp.kobespiral.horie.todo.entity.Member;
import jp.kobespiral.horie.todo.entity.ToDo;
import jp.kobespiral.horie.todo.service.MemberService;
import jp.kobespiral.horie.todo.service.ToDoService;

@Controller
public class ToDoController {
    @Autowired
    MemberService mService;
    @Autowired
    ToDoService todoService;

    @GetMapping("/")
    String login(@ModelAttribute LoginForm form, Model model) {
        model.addAttribute("LoginForm", form);
        return "index";
    }

    @GetMapping("/{mid}/login")
    String login(@Validated @ModelAttribute(name = "LoginForm") LoginForm form,
            BindingResult bindingResult, @RequestParam String mid, Model model) {
        if (bindingResult.hasErrors()) {
            // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
            return login(form, model);
        }

        return "redirect:/" + mid + "/todos";
    }

    @GetMapping("/{mid}/todos")
    String todoList(@PathVariable String mid, @ModelAttribute(name = "ToDoForm") ToDoForm form, Model model) {
        model.addAttribute("ToDoForm", form);
        model.addAttribute("mid", mid);
        Member m = mService.getMember(mid);
        model.addAttribute("name", m.getName());
        List<ToDo> todo = todoService.getToDoList(mid);
        List<ToDo> done = todoService.getDoneList(mid);
        model.addAttribute("todo", todo);
        model.addAttribute("done", done);
        return "list";
    }

    @GetMapping("/{mid}/alltodos")
    String alltodoList(@PathVariable String mid, Model model) {
        model.addAttribute("mid", mid);
        Member m = mService.getMember(mid);
        model.addAttribute("name", m.getName());
        List<ToDo> todo = todoService.getToDoList();
        List<ToDo> done = todoService.getDoneList();
        model.addAttribute("todo", todo);
        model.addAttribute("done", done);
        return "alllist";
    }

    @GetMapping("/{mid}/{seq}/done")
    String done(@PathVariable String mid, @PathVariable Long seq, Model model) {
        todoService.updateToDo(seq);
        return "redirect:/" + mid + "/todos";
    }

    @PostMapping("/{mid}/register")
    String register(@PathVariable String mid, @Validated @ModelAttribute(name = "ToDoForm") ToDoForm form,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
            System.out.println("えらー");
            return todoList(mid, form, model);
        }
        todoService.createToDo(mid, form);
        return "redirect:/" + mid + "/todos";
    }

}
