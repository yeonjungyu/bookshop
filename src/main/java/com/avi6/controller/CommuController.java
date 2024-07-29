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
    
    @GetMapping("/view-talk/{id}")
    public String viewTalk(@PathVariable("id") Long id, Model model) {
        Optional<BookTalk> talk = bookTalkService.getBookTalkById(id);
        model.addAttribute("talk", talk);
        return "community/view-talk";
    }

    @GetMapping("/edit-talk/{id}")
    public String editTalkForm(@PathVariable("id") Long id, Model model, Principal principal) {
        // 사용자 인증 확인
        if (principal == null) {
            return "redirect:/login";
        }

        // 글 수정 폼을 위해 해당 글 정보 가져오기
        Optional<BookTalk> talk = bookTalkService.getBookTalkById(id);

        // 현재 사용자 정보 전달
        model.addAttribute("username", principal.getName());
        model.addAttribute("talk", talk);
        return "community/edit-talk";
    }

    @PutMapping("/edit-talk/{id}")
    public String editTalk(@PathVariable("id") Long id, @ModelAttribute("talk") BookTalk updatedTalk,
                           Principal principal) {
        // 사용자 인증 확인
        if (principal == null) {
            return "redirect:/login";
        }

        // 기존 글 정보 가져오기
        Optional<BookTalk> optionalExistingTalk = bookTalkService.getBookTalkById(id);
        
        // 옵셔널에서 실제 객체를 가져오지 못한 경우 처리
        if (!optionalExistingTalk.isPresent()) {
            return "redirect:/community";
        }
        
        BookTalk existingTalk = optionalExistingTalk.get();

        // 권한 확인: 현재 사용자가 글의 작성자인지 검사
        if (!existingTalk.getAuthor().getUsername().equals(principal.getName())) {
            return "redirect:/community";
        }

        // 기존 글 정보 업데이트
        existingTalk.setTitle(updatedTalk.getTitle());
        existingTalk.setContent(updatedTalk.getContent());

        // 수정된 글 저장
        bookTalkService.saveBookTalk(existingTalk);

        // 수정 후 상세 페이지로 리다이렉트
        return "redirect:/community/view-talk/" + id;
    }
    
    @DeleteMapping("/delete-talk/{id}")
    public String deleteTalk(@PathVariable("id") Long id, Principal principal) {
        // 사용자 인증 확인
        if (principal == null) {
            return "redirect:/login";
        }

        // 글 삭제: 해당 ID의 글을 삭제합니다.
        bookTalkService.deleteBookTalk(id);

        // 삭제 후 커뮤니티 페이지로 리다이렉트
        return "redirect:/community";
    }


}
