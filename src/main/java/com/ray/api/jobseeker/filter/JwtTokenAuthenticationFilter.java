package com.ray.api.jobseeker.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.api.jobseeker.model.entity.User;
import com.ray.api.jobseeker.repository.UserRepository;
import com.ray.api.jobseeker.util.JwtTokenUtil;
import com.ray.api.jobseeker.util.ObjectMapperUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);
        log.info("Token: "+token);
        if (token != null) {
            try {
                Claims claims = jwtTokenUtil.parseToken(token);
                Query query = new Query();
                query.addCriteria(Criteria.where("active_token").is(token));
                User user = mongoTemplate.findOne(query, User.class);

                if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null && user != null) {
                    log.info("{}", user.getUsername());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.warn("Active token not found");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
            } catch (Exception e) {
                // Invalid token
                log.warn("Invalid JWT token: {}", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer "
        }
        return null;
    }
}