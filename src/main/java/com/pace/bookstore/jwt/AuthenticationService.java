package com.pace.bookstore.jwt;

import com.pace.bookstore.entity.Role;
import com.pace.bookstore.entity.Token;
import com.pace.bookstore.entity.User;
import com.pace.bookstore.exceptions.BookStoreException;
import com.pace.bookstore.repository.TokenRepository;
import com.pace.bookstore.repository.UserRepository;
import com.pace.bookstore.repository.UserRoleXrefRepository;
import com.pace.bookstore.utility.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final UserRoleXrefRepository userRoleXrefRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BookStoreException("Invalid email/password supplied");
        }

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

        List<Role> roles = userRoleXrefRepository.findUserRoleByUserId(user.getId());

        if (roles.isEmpty()) {
            throw new Exception("Roles not found for the user");
        }

        String jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

}