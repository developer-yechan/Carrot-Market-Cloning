package clone.carrotMarket.api;

import clone.carrotMarket.dto.ErrorResponse400;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    // 400 예외처리 핸들러
    @ExceptionHandler
    public ResponseEntity<ErrorResponse400> handleExceptions(RuntimeException e){
        log.error(e.getMessage());
        ErrorResponse400 errorDTO = new ErrorResponse400(400,"잘못된 요청입니다.");
        return ResponseEntity.badRequest().body(errorDTO);
    }

}
