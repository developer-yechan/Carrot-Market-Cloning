package clone.carrotMarket.web.security;

import clone.carrotMarket.dto.ErrorResponse400;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch(Exception e){
            setErrorResponse(response,e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, Exception exception) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.error(exception.getMessage());
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse400 errorResponse = new ErrorResponse400(400, "잘못된 요청입니다.");
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
