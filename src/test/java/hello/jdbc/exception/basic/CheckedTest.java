package hello.jdbc.exception.basic;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {



    @Test
    void checked_catch(){
        Service service = new Service();
        service.catchException();
    }


    static class MyCheckedException extends Exception {

        public MyCheckedException(String message){
            super(message);
        }
    }



    static class Service {

        Repository repository = new Repository();


        public void catchException(){
            try {
                repository.callException();
            } catch (MyCheckedException e) {
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

    }

    static class Repository {
        public void callException() throws MyCheckedException {
            throw new MyCheckedException("exception 발생");
        }
    }

}
