package clone.carrotMarket.api;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        log.info("error 1 : {}",ex.getStackTrace());
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class})
    public ResponseEntity<Map<String,String>> handleIllegalArgumentExceptions(RuntimeException ex){
        log.info("error 2 : {}",ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("code","400");
        errors.put("message",ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidFormatExceptionExceptions(InvalidFormatException ex){
        log.info("error 3 : {}",ex.getMessage());
        Object value = ex.getValue();
        Map<String, Object> errors = new HashMap<>();
        errors.put("code","400");
        errors.put("message", "잘못된 입력 값입니다.");
        errors.put("errorValue",value);
        return ResponseEntity.badRequest().body(errors);
    }
}
