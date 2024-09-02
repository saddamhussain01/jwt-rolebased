package com.easylearning.filters;

import com.easylearning.services.jwtService.UserDetailsServiceImpl;
import com.easylearning.services.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

                        //OncePerRequestFilter is a abstract class.
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         String authHeader = request.getHeader("Authorization");
         String token = null;
         String username = null;



         if (authHeader != null && authHeader.startsWith("Bearer ")){
             token = authHeader.substring(7);
             username = jwtUtil.extractUsername(token); //extract username from token
         }


         /*
         SecurityContextHolder: Think of this as a place where Spring Security keeps track of important security information
                                while your program is running.

          getAuthentication(): This method retrieves information about the currently logged-in user (or authentication details)
                               from the security context.

          */

         if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

             UserDetails userDetails = userDetailsService.loadUserByUsername(username);

             /*

             jwtUtil.validateToken(token, userDetails):
             This function checks if a given token is valid according to the rules defined in the jwtUtil.
             If it returns true, it means the token is valid.

             */
             if(jwtUtil.validateToken(token,userDetails)){

                 /*

                 If the token is valid, this line creates an authentication token for the user.
                 It includes information about the user (userDetails),
                 such as their username and authorities (roles or permissions).

                 */
                 UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

                 /*
                 This line adds details about the user's request,
                 like the IP address or browser details, to the authentication token.
                  */
                 authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                 /*
                   Finally, this line sets the authentication token we created as the current authentication in the security context.
                   This means that the user is now considered authenticated by the system.
                 */
                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
             }
         }

         filterChain.doFilter(request,response);
         /*
         So, filterChain.doFilter(request, response); essentially means:
             Take this request, process it through the next filter in the security chain,
             and prepare a response to send back to the client.
          */

    }
}
