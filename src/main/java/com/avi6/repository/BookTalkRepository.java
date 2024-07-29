package com.avi6.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avi6.entity.BookTalk;
import com.avi6.entity.User;

public interface BookTalkRepository extends JpaRepository<BookTalk, Long>{

	List<BookTalk> findByAuthor(User author);
}
