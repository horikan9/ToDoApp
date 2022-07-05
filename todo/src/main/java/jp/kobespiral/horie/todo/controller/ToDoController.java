package jp.kobespiral.horie.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.kobespiral.horie.todo.dto.LoginForm;
import jp.kobespiral.horie.todo.dto.MemberForm;
import jp.kobespiral.horie.todo.dto.ToDoForm;
import jp.kobespiral.horie.todo.dto.UserDetailsImpl;
import jp.kobespiral.horie.todo.entity.Member;
import jp.kobespiral.horie.todo.entity.ToDo;
import jp.kobespiral.horie.todo.exception.ToDoAppException;
import jp.kobespiral.horie.todo.service.MemberService;
import jp.kobespiral.horie.todo.service.ToDoService;

@Controller
public class ToDoController {
    @Autowired
    MemberService mService;
    @Autowired
    ToDoService tService;

    /**
     * トップページ
     */
    @GetMapping("/sign_in")
    String showIndex(@RequestParam Map<String, String> params, @ModelAttribute LoginForm form, Model model) {
        // パラメータ処理．ログアウト時は?logout, 認証失敗時は?errorが帰ってくる（WebSecurityConfig.java参照）
        if (params.containsKey("sign_out")) {
            model.addAttribute("message", "サインアウトしました");
        } else if (params.containsKey("error")) {
            model.addAttribute("message", "サインインに失敗しました");
        }
        // model.addAttribute("loginForm", loginForm);
        return "signin";
    }

    /**
     * ログイン処理．midの存在確認をして，ユーザページにリダイレクト
     */
    @GetMapping("/sign_in_success")
    String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (m.getRole().equals("ADMIN")) {
            return "redirect:/admin/register";
        }
        return "redirect:/" + m.getMid() + "/todos";
    }

    /**
     * ユーザのToDoリストのページ
     */
    @GetMapping("/{mid}/todos")
    String showToDoList(@PathVariable String mid, @ModelAttribute(name = "ToDoForm") ToDoForm form, Model model) {
        checkIdentity(mid);

        Member m = mService.getMember(mid);
        model.addAttribute("member", m);
        model.addAttribute("ToDoForm", form);
        List<ToDo> todos = tService.getToDoList(mid);
        model.addAttribute("todos", todos);
        List<ToDo> dones = tService.getDoneList(mid);
        model.addAttribute("dones", dones);
        return "list";
    }

    /**
     * 全員のToDoリストのページ
     */
    @GetMapping("/{mid}/todos/all")
    String showAllToDoList(@PathVariable String mid, Model model) {
        checkIdentity(mid);
        Member m = mService.getMember(mid);
        model.addAttribute("member", m);
        List<ToDo> todos = tService.getToDoList();
        model.addAttribute("todos", todos);
        List<ToDo> dones = tService.getDoneList();
        model.addAttribute("dones", dones);
        return "alllist";
    }

    /**
     * ToDoの作成．作成処理後，ユーザページへリダイレクト
     */
    @PostMapping("/{mid}/todos")
    String createToDo(@PathVariable String mid, @Validated @ModelAttribute(name = "ToDoForm") ToDoForm form,
            BindingResult bindingResult, Model model) {
        checkIdentity(mid);

        if (bindingResult.hasErrors()) {
            return showToDoList(mid, form, model);
        }
        tService.createToDo(mid, form);
        return "redirect:/" + mid + "/todos";
    }

    /**
     * ToDoの完了．完了処理後，ユーザページへリダイレクト
     */
    @GetMapping("/{mid}/todos/{seq}/done")
    String doneToDo(@PathVariable String mid, @PathVariable Long seq, Model model) {
        checkIdentity(mid);
        tService.done(mid, seq);
        return "redirect:/" + mid + "/todos";
    }

    /**
     * 認可チェック．与えられたmidがログイン中のmidに等しいかチェックする
     */
    private void checkIdentity(String mid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (!mid.equals(m.getMid())) {
            throw new ToDoAppException(ToDoAppException.INVALID_TODO_OPERATION,
                    m.getMid() + ": not authorized to access resources of " + mid);
        }
    }

}

// package jp.kobespiral.horie.todo.controller;

// import java.util.List;

// import javax.websocket.server.PathParam;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import jp.kobespiral.horie.todo.dto.LoginForm;
// import jp.kobespiral.horie.todo.dto.ToDoForm;
// import jp.kobespiral.horie.todo.entity.Member;
// import jp.kobespiral.horie.todo.entity.ToDo;
// import jp.kobespiral.horie.todo.service.MemberService;
// import jp.kobespiral.horie.todo.service.ToDoService;

// @Controller
// public class ToDoController {
// @Autowired
// MemberService mService;
// @Autowired
// ToDoService todoService;

// @GetMapping("/")
// String login(@ModelAttribute LoginForm form, Model model) {
// model.addAttribute("LoginForm", form);
// return "index";
// }

// @GetMapping("/{mid}/login")
// String login(@Validated @ModelAttribute(name = "LoginForm") LoginForm form,
// BindingResult bindingResult, @RequestParam String mid, Model model) {
// if (bindingResult.hasErrors()) {
// // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
// return login(form, model);
// }

// return "redirect:/" + mid + "/todos";
// }

// @GetMapping("/{mid}/todos")
// String todoList(@PathVariable String mid, @ModelAttribute(name = "ToDoForm")
// ToDoForm form, Model model) {
// model.addAttribute("ToDoForm", form);
// model.addAttribute("mid", mid);
// Member m = mService.getMember(mid);
// model.addAttribute("name", m.getName());
// List<ToDo> todo = todoService.getToDoList(mid);
// List<ToDo> done = todoService.getDoneList(mid);
// model.addAttribute("todo", todo);
// model.addAttribute("done", done);
// return "list";
// }

// @GetMapping("/{mid}/alltodos")
// String alltodoList(@PathVariable String mid, Model model) {
// model.addAttribute("mid", mid);
// Member m = mService.getMember(mid);
// model.addAttribute("name", m.getName());
// List<ToDo> todo = todoService.getToDoList();
// List<ToDo> done = todoService.getDoneList();
// model.addAttribute("todo", todo);
// model.addAttribute("done", done);
// return "alllist";
// }

// @GetMapping("/{mid}/{seq}/done")
// String done(@PathVariable String mid, @PathVariable Long seq, Model model) {
// todoService.doneToDo(seq);
// return "redirect:/" + mid + "/todos";
// }

// @PostMapping("/{mid}/register")
// String register(@PathVariable String mid, @Validated @ModelAttribute(name =
// "ToDoForm") ToDoForm form,
// BindingResult bindingResult, Model model) {
// if (bindingResult.hasErrors()) {
// // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
// System.out.println("えらー");
// return todoList(mid, form, model);
// }
// todoService.createToDo(mid, form);
// return "redirect:/" + mid + "/todos";
// }

// }
