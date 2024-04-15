package com.financing.app.bootstrap_module.app_config_security;

import com.financing.app.bootstrap_module.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRateLimitFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var key = "rate_limit:" + request.getRemoteAddr();

        var requestCount = valueOperations.increment(key);
        if (requestCount == null){
            filterChain.doFilter(request, response);
            return;
        }
        if (requestCount == 1) {
            redisTemplate.expire(key, 10, TimeUnit.SECONDS);
        }
        if (requestCount > 2) {
            var errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.TOO_MANY_REQUESTS.value(),
                    "Too Many Requests",
                    "Rate limit exceeded",
                    request.getRequestURI()
            );

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(ErrorResponse.toJsonString(errorResponse));
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
