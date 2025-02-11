package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id // 이 필드를 primary key로 간주한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key는 자동 생성되는 값이다.
    private Long id = null;

    @Column(nullable = false, length = 20, name = "name") // == name varchar(20)
    private String name;
    private Integer age; // null도 가능하고 길이제한도 없이 특별한 설정을 하지 않아도 되는 경우 @Column 생략 가능!

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // 연관관계의 주인은 UserLoanHistory -> 연관관계의 주인이 아닌쪽에 mappedBy를 해야함
    // cascade옵션: user가 삭제될 때 user와 연결된 대출기록까지 삭제 가능
    // orphanRemoval옵션: 객체간의 관계가 끊어진 데이터를 자동으로 제거하는 옵션
    // -> ex) user가 책1,2를 대출받은상황에서 책1 대출 기록과의 관계를 끊으면 데이터상에서도 삭제됨
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    protected User() {
    } // JPA를 사용하기 위해서는 기본생성자가 필요!

    public User(String name, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
        }
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void loanBook(String bookName){
        this.userLoanHistories.add(new UserLoanHistory(this,bookName));
    }

    public void returnBook(String bookName){
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                .filter(history -> history.getBookName().equals(bookName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        targetHistory.doReturn();
    }

}
