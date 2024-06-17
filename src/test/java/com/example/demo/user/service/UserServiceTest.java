package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@TestPropertySource("classpath:application-test.properties")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "thstkddnr20@naver.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("son");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        String email = "thstkddnr30@naver.com";

        // when
        // then
        assertThatThrownBy(()-> userService.getByEmail(email)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        // when
        UserEntity result = userService.getById(3);

        // then
        assertThat(result.getNickname()).isEqualTo("son");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(()-> userService.getById(2)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("thstkddnr20@gmail.com")
                .address("chuncheon")
                .nickname("son123")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity result = userService.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다() {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Incheon")
                .build();

        // when
        UserEntity updated = userService.update(3, userUpdate);

        // then
        assertThat(updated.getAddress()).isEqualTo("Incheon");
    }

    @Test
    void user_를_로그인시키면_last_login_at이_변경된다() {
        // given
        // when
        userService.login(3);

        // then
        UserEntity userEntity = userService.getById(3);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L); //equalTo로 수정 필요
    }

    @Test
    void PENDING_상태의_사용자는_로그인하여_ACTIVE_상태로_활성화() {
        // given
        // when
        userService.verifyEmail(2, "AAAAAAAAAAA-AAAA-AAAAB");

        // then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증코드를_받으면_통과X() {
        // given
        // when
        // then
        assertThatThrownBy(() -> userService.verifyEmail(2, "AAA")).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}