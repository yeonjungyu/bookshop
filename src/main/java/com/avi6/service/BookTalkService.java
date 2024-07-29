package com.avi6.service;

import com.avi6.dto.BookTalkDTO;
import com.avi6.entity.BookTalk;
import com.avi6.entity.User;
import com.avi6.repository.BookTalkRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookTalkService {

    private final BookTalkRepository bookTalkRepository;

    @Autowired
    public BookTalkService(BookTalkRepository bookTalkRepository) {
        this.bookTalkRepository = bookTalkRepository;
    }

    // 모든 BookTalk 조회
    public List<BookTalk> getAllBookTalks() {
        return bookTalkRepository.findAll();
    }

    // ID로 BookTalk 조회
    public Optional<BookTalk> getBookTalkById(Long id) {
        return bookTalkRepository.findById(id);
    }

    // BookTalk 저장
    public BookTalk saveBookTalk(BookTalk bookTalk) {
        return bookTalkRepository.save(bookTalk);
    }

    // BookTalk 삭제
    public void deleteBookTalk(Long id) {
        bookTalkRepository.deleteById(id);
    }
}