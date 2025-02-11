package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 트랜잭션 적용(commit, rollback) -> 해당 함수를 한몸처럼 적용!
    // 아래 있는 함수가 시작될 때 start transaction;을 해준다(트랜잭션을 시작!)
    // 함수가 예외 없이 잘 끝났다면 commit
    // 혹시라도 문제가 있다면 rollback

    // 영속성 컨텍스트 -> 트랜잭션이 시작될때 시작되고 종료될때 종료됨
    // 1. 변경 감지: 정보를 변경하는 경우, save코드를 쓰지않아도 자동으로 변경된 정보로 저장해줌
    // 2. 쓰기 지연: sql문을 하나씩 적용하는게 아니라 한번에 적용시킴
    // 3. 1차 캐싱: 1차 코드에 해당되는 정보를 2차 코드에서 다시 DB에서 가져오지 않아도 1차 코드에서 조회한 정보를 가져와 사용 가능
    @Transactional // 트랜잭션 적용
    // JPA 기능(save): 주어지는 객체를 저장하고나 업데이트 시켜준다.
    public void saveUser(UserCreateRequest request) {
        userRepository.save(new User(request.getName(), request.getAge()));
    }

    @Transactional(readOnly = true)
    // JPA 기능(findAll): 주어지는 객체가 매핑된 테이블의 모든 데이터를 가져온다.
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    // JPA 기능(findById): id를 기준으로 특정한 1개의 데이터를 가져온다.
    public void updateUser(UserUpdateRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);

        user.updateName(request.getName());
        // userRepository.save(user); -> 영속성 컨텍스트 특징(변경 감지)을 통해 쓰지 않아도 자동 저장됨
    }

    @Transactional
    // JPA 기능: 필요한 함수(findByName)을 UserRepository.java에서 생성해서 사용 가능
    public void deleteUser(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(IllegalArgumentException::new);

        userRepository.delete(user);
    }
}
