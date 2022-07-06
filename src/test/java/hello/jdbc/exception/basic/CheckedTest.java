package hello.jdbc.exception.basic;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {



    //exception catch 테스트
    @Test
    void checked_catch(){
        Service service = new Service();
        service.catchException();
    }


    //exception throw 테스트
    @Test
    void checked_throw(){
        Service service = new Service();

        Assertions.assertThatThrownBy(() -> service.throwException()).isInstanceOf(MyCheckedException.class);
    }


    static class MyCheckedException extends Exception {

        public MyCheckedException(String message){
            super(message);
        }
    }



    static class Service {

        Repository repository = new Repository();


        //체크예외 catch
        public void catchException(){
            try {
                repository.callException();
            } catch (MyCheckedException e) {
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        //체크예외 throw
        public void throwException() throws MyCheckedException {
            repository.callException();
        }

    }

    static class Repository {
        public void callException() throws MyCheckedException {
            throw new MyCheckedException("exception 발생");
        }
    }

}
