package clone.carrotMarket.api;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class})
    public ResponseEntity<Map<String,String>> handleIllegalArgumentExceptions(RuntimeException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidFormatExceptionExceptions(InvalidFormatException ex){
        Object value = ex.getValue();
        Map<String, Object> error = new HashMap<>();
        error.put("messsage", "잘못된 입력 값입니다.");
        error.put("errorValue",value);
        return ResponseEntity.badRequest().body(error);
    }
}
