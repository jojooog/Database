package hello.jdbc.service;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class MemberServiceV1Test {

    //상수로 정의해두기
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "memberEX";

    private MemberRepositoryV1 memberRepositoryV1;
    private MemberServiceV1 memberServiceV1;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL,
                ConnectionConst.USERNAME, ConnectionConst.PASSWORD);

        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
        memberServiceV1 = new MemberServiceV1(memberRepositoryV1);
    }

    @Test
    @DisplayName("계좌이체 처리하기")
    void accountTransfer() throws SQLException {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);

        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(memberB);

        memberServiceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);


        Member findMemberA = memberRepositoryV1.findById(memberA.getMemberId());
        Member findMemberB = memberRepositoryV1.findById(memberB.getMemberId());

        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);

    }

}