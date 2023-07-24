package clone.carrotMarket.api;

import clone.carrotMarket.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    // 400 예외처리 핸들러
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleExceptions(Exception e){
        log.error("400 error : {}",e.getMessage());
        ErrorDTO errorDTO = new ErrorDTO(400,e.getMessage());
        return ResponseEntity.badRequest().body(errorDTO);

    }

}
