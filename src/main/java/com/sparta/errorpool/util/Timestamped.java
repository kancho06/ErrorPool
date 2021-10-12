package com.sparta.errorpool.util;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter     // 일괄적으로 get 함수 자동 생성, ****** 까먹지 않아야한다.
@MappedSuperclass   // 멤버 변수가 컬럼으로 되도록
@EntityListeners(AuditingEntityListener.class)  // 다른 값이 변경되었을 때, 멤버 변수가 자동으로 변경되도록
public abstract class Timestamped {

//  게시글 생성시간
    @CreatedDate
    private LocalDateTime createdAt;

//  게시글 수정시간
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
