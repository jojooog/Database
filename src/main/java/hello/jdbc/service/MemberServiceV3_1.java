package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

   // private final DataSource dataSource;

    private final PlatformTransactionManager transactionManager;

    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        //트랜잭션 start
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            //비즈니스 로직 수행
            //커넥션을 넘기지 않아도 된다
            logic(fromId,toId, money );

            //성공하면 커밋
            transactionManager.commit(status);

            //예외가 발생할 경우 처리
        } catch(Exception e) {

            transactionManager.rollback(status);
            throw  new IllegalStateException(e);

        } //트랜잭션이 커밋되거나 롤백되면 알아서 커넥션이 종료된다



    }


    private void validation(Member toMember){
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("계좌이체중 예외 발생");
        }
    }

    private void logic(String fromId, String toId, int money) throws Exception {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);

        //예외발생 테스트
        validation(toMember);

        //검증에서 에러가 발생하면 그 다음 update 로직으로 못넘어간다
        //커밋 이나 롤백
        memberRepository.update(toId, toMember.getMoney() + money);

    }


}
