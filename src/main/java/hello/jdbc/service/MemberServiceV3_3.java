package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * @Transactional AOP 적용해보기
 */
@Slf4j
public class MemberServiceV3_3 {

    //트랜잭션 관련 코드 제거
   private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws Exception {

        logic(fromId,toId, money );

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


    private void validation(Member toMember){
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("계좌이체중 예외 발생");
        }
    }


}
