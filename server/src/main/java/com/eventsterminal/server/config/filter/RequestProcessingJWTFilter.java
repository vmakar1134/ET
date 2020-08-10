package com.eventsterminal.server.config.filter;

import com.eventsterminal.server.config.model.UserPrincipal;
import com.eventsterminal.server.config.service.impl.CustomUserDetailsService;
import com.eventsterminal.server.config.service.impl.TokenProviderServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestProcessingJWTFilter extends OncePerRequestFilter {

    private final TokenProviderServiceImpl tokenProviderService;

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public RequestProcessingJWTFilter(TokenProviderServiceImpl tokenProviderService,
                                      CustomUserDetailsService customUserDetailsService) {
        this.tokenProviderService = tokenProviderService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        try {
            Jws<Claims> claimsJws = tokenProviderService.getClaimsJws(jwt);
            if (StringUtils.hasText(jwt) && claimsJws != null) {
                Long userAuthId = tokenProviderService.getUserIdFromClaims(claimsJws);
                UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserById(userAuthId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, "", userPrincipal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    // TODO: 8/10/20 refactor bearerToken.substring(7)
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
