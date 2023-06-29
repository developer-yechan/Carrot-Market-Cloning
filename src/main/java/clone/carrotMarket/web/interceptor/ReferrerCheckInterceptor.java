package clone.carrotMarket.web.interceptor;

import clone.carrotMarket.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class ReferrerCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referrer = request.getHeader("Referer");
        String host = request.getHeader("host");
        if (referrer == null || !referrer.contains(host)) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
