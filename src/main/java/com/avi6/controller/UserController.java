package com.avi6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.avi6.dto.UserDTO;
import com.avi6.entity.User;
import com.avi6.service.UserService;
import com.avi6.user.DuplicateUsernameException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class UserController {
	
	private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

	@GetMapping("/index")
	public String index() {
        return "index"; 
	}
	
	
    // 회원가입 폼 요청 처리
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // 회원가입 폼 템플릿 이름
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") User user, Model model) {
        try {
        	userService.saveUser(user); 
        	System.out.println("성공");
            return "redirect:/login"; 
		} catch (DuplicateUsernameException e) {
			model.addAttribute("error", "중복된 이메일입니다. 다른 이메일을 사용해주세요."); // 에러 메시지 전달
			System.out.println("실패");
			return "register"; // 회원가입 페이지 다시 보여주기
		}
    }
    // 로그인 페이지 요청 처리
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // 로그인 폼 템플릿 이름
    }
   
}
