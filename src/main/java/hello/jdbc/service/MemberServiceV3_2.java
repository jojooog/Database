package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;




@Slf4j
public class MemberServiceV3_2 {

   // private final DataSource dataSource;
    // private final PlatformTransactionManager transactionManager;

    private final TransactionTemplate template;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.template = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        template.executeWithoutResult((status) -> {
            //비즈니스 로직
            try {
                logic(fromId,toId, money );
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });

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
