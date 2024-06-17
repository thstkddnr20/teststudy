package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_를_사용하여_유저_데이터를_가져올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_를_사용하여_유저_데이터를_가져올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("thstkddnr20@naver.com", UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("thstkddnr20@naver.com", UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }
}