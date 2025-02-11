package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // findByName함수 생성! -> 함수이름에 따라 SQL문이 자동 생성된다
    // fingByName == SELECT * FROM user WHERE name = ?
    Optional<User> findByName(String name); // 반환타입은 User 값이없다면 null이 반환

    // By앞에 오는 구절들
    // find: 1건을 가져온다. 반환 타입은 객체가 될 수도 있고, Optional<타입>이 될 수도 있다.
    // findAll: 쿼리의 결과물이 N개인 경우 사용 LIST<타입> 반환.
    // exists: 쿼리 결과가 존재하는지 확인. 반환 타입은 boolean
    // count: SQL의 결과 개수를 센다. 반환 타입은 long이다.

    // By뒤에 오는 구절들
    // And나 Or로 조합할 수도 있다.
    // ex) List<User> findAllByNameAndAge(String name, int age); == SELECT * FROM user WHERE name = ? AND age = ?;
    // GreaterThan: 초과
    // GreaterThanEqual: 이상
    // LessThan: 미만
    // LessThanEqual: 이하
    // Between: 사이에
    // ex) List<User> findAllByAgeBetween(int startAge, int endAge); == SELECT * FROM user WHERE age BETWEEN ? AND ?;
    // StartsWith: ~로 시작하는
    // EndsWith: ~로 끝나는
}
