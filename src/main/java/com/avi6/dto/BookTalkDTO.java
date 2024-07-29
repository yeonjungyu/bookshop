package com.avi6.dto;

import java.util.Date;

public class BookTalkDTO {

    private Long id;
    private String title;
    private String content;
    private String authorName; // 작성자의 이름을 저장하기 위한 필드
    private Date createdAt;

    // 생성자
    public BookTalkDTO() {
    }

    public BookTalkDTO(Long id, String title, String content, String authorName, Date createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = createdAt;
    }

    // Getter, Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

 
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
