package com.paipeng.iot.security;


import com.paipeng.iot.config.ApplicationConfig;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.repository.UserRepository;
import com.paipeng.iot.service.CustomUserDetailsService;
import com.paipeng.iot.service.JwtService;
import io.jsonwebtoken.*;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
//    private final static Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    private final static String HEADER = "Authorization";
    private final static String PREFIX = "Bearer ";
    //public static final String SECRET = "";

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtService jwtService;
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal " + request.getRemoteHost() + " " + request.getMethod() + " " + request.getRequestURI());

        try {
            if (checkJWTToken(request, response)) {
                logger.info("jwt token found");
                final String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
                logger.info("jwtToken: " + jwtToken);

                final String username = jwtService.extraceUsername(jwtToken);
                logger.info("username: " + username);
                if (username != null) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        User user = customUserDetailsService.loadUserByUsername(username);
                        if (jwtService.isTokenValid(jwtToken, user)) {
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
                } else {
                    logger.error("validateToken failed");
                }
/*
                User user = userRepository.findByToken(jwtToken);

                Claims claims = validateToken(jwtToken, user);
                if (claims != null && claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims, user);
                } else {
                    logger.error("validateToken failed");
                    SecurityContextHolder.clearContext();
                }
 */
            } else {
                logger.error("checkJWTToken failed");
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            logger.error("doFilterInternal ExpiredJwtException: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            logger.error("doFilterInternal exception local: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            logger.error("doFilterInternal exception21: " + e.getMessage());
            if (e.getMessage().endsWith("java.lang.Exception: 400")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 403")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 401")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 404")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 409")) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
            } else if (e.getMessage().startsWith("JWT expired")) {
                logger.error("doFilterInternal JWT expired -> 401");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } else {
                logger.error("exception not handle");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    private Claims validateToken(String jwtToken, User user) {
        logger.info("validateToken");
        if (user != null) {
            logger.info("local SECRET: " + user.getToken());
            return Jwts.parser().setSigningKey(applicationConfig.getSecurityJwtSecret()).build().parseClaimsJws(jwtToken).getBody();
        }
        return null;
    }

    /**
     * Authentication method in Spring flow
     *
     * @param claims claims
     * @param user   user
     */
    private void setUpSpringAuthentication(Claims claims, User user) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }

}
