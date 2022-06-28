package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;

    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        //커넥션 얻기
        Connection con = dataSource.getConnection();

        try {
            con.setAutoCommit(false); //트랜잭션 시작

            //비즈니스 로직 수행

            Member fromMember = memberRepository.findById(con, fromId);
            Member toMember = memberRepository.findById(con, toId);

            memberRepository.update(con, fromId, fromMember.getMoney() - money);

            //예외발생 테스트
            validation(toMember);

            //검증에서 에러가 발생하면 그 다음 update 로직으로 못넘어간다
            //커밋 이나 롤백
            memberRepository.update(con, toId, toMember.getMoney() + money);

            //정상적으로 수행되면 commit
            con.commit();

            //예외가 발생할 경우 처리
        } catch(Exception e) {

            //롤백하고 예외 던지기기
           con.rollback();
            throw  new IllegalStateException(e);


        } finally {
            if(con != null) {
                try {
                    //트랜잭션을 자동커밋 모드로 돌려놓기
                    con.setAutoCommit(true);

                    //트랜잭션 종료
                    con.close();



                } catch(Exception e) {
                    //에러 로그 찍기
                    log.info("error", e);
                }
            }
        }



    }


    private void validation(Member toMember){
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("계좌이체중 예외 발생");
        }
    }


}
