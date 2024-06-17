package com.example.demo.post.service;


import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@TestPropertySource("classpath:application-test.properties")
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        PostEntity result = postService.getById(4);

        // then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("thstkddnr20@naver.com");
    }

    @Test
    void postCreateDto를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(3)
                .content("chuncheon")
                .build();

        // when
        PostEntity result = postService.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("chuncheon");
        assertThat(result.getCreatedAt()).isGreaterThan(0);

    }

    @Test
    void postupdateDto를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("Incheon")
                .build();

        // when
        PostEntity updated = postService.update(4, postUpdate);

        // then
        assertThat(updated.getContent()).isEqualTo("Incheon");
    }

}