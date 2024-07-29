package com.avi6.controller;

import com.avi6.dto.BookTalkDTO;
import com.avi6.entity.BookTalk;
import com.avi6.entity.User;

import com.avi6.service.BookTalkService;
import com.avi6.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/community")
public class CommuController {

    private final BookTalkService bookTalkService;
    private final UserService userService;

    @Autowired
    public CommuController(BookTalkService bookTalkService, UserService userService) {
        this.bookTalkService = bookTalkService;
        this.userService = userService;
    }

    @GetMapping("")
    public String community(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<BookTalk> bookTalks = bookTalkService.getAllBookTalks();
        model.addAttribute("bookTalks", bookTalks);
        model.addAttribute("username", userDetails.getUsername()); // 현재 로그인한 사용자의 이름 전달
        return "community/community";
    }

    @GetMapping("/new-talk")
    public String newTalkForm(Model model, Principal principal) {
        if (principal == null) {
            // 인증되지 않은 사용자 처리 (로그인 페이지로 리다이렉트)
            return "redirect:/login";
        }

        model.addAttribute("bookTalkDTO", new BookTalkDTO());
        return "community/new-talk";
    }

    @PostMapping("/new-talk")
    public String createTalk(@ModelAttribute("bookTalkDTO") BookTalkDTO bookTalkDTO,
                             Principal principal) {
        if (principal == null) {
            // 인증되지 않은 사용자 처리 (로그인 페이지로 리다이렉트)
            return "redirect:/login";
        }

        // 사용자 정보 가져오기
        String username = principal.getName();
        User author = userService.findByUsername(username);

        // 새로운 BookTalk 객체 생성
        BookTalk newBookTalk = new BookTalk();
        newBookTalk.setTitle(bookTalkDTO.getTitle());
        newBookTalk.setContent(bookTalkDTO.getContent());
        newBookTalk.setAuthor(author);
        newBookTalk.setCreatedAt(new Date());

        // BookTalk 저장
        bookTalkService.saveBookTalk(newBookTalk);

        // 커뮤니티 페이지로 리다이렉트
        return "redirect:/community";
    }
    
    @GetMapping("/edit-talk/{id}")
    public String editTalkForm(@PathVariable Long id, Model model) {
        Optional<BookTalk> optionalBookTalk = bookTalkService.getBookTalkById(id);
        BookTalk bookTalk = optionalBookTalk.orElse(null); // Optional에서 BookTalk 추출

        if (bookTalk == null) {
            // 처리할 예외 상황이나 에러 처리 로직 추가
            throw new RuntimeException("BookTalk not found with id: " + id);
        }

        // BookTalk을 DTO로 변환하여 템플릿에 전달
        BookTalkDTO bookTalkDTO = new BookTalkDTO();
        bookTalkDTO.setId(bookTalk.getId());
        bookTalkDTO.setTitle(bookTalk.getTitle());
        bookTalkDTO.setContent(bookTalk.getContent());
        model.addAttribute("bookTalkDTO", bookTalkDTO);

        return "community/edit-talk";
    }

    @PostMapping("/edit-talk/{id}")
    public String updateTalk(@PathVariable Long id, @ModelAttribute("bookTalkDTO") BookTalkDTO bookTalkDTO) {
        // 글 id를 기반으로 해당 글을 가져옴
        Optional<BookTalk> optionalExistingTalk = bookTalkService.getBookTalkById(id);
        
        // Optional에서 BookTalk 객체 추출
        BookTalk existingTalk = optionalExistingTalk.orElseThrow(() -> new RuntimeException("BookTalk not found with id: " + id));
        
        // 업데이트할 내용 설정
        existingTalk.setTitle(bookTalkDTO.getTitle());
        existingTalk.setContent(bookTalkDTO.getContent());
        
        // 글 업데이트
        bookTalkService.saveBookTalk(existingTalk);
        
        // 수정된 글의 상세 페이지로 리다이렉트 또는 커뮤니티 페이지로 리다이렉트
        return "redirect:/community";
    }


}
