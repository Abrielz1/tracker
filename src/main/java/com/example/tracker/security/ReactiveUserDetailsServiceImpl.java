package com.example.tracker.security;

import com.example.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(final String login) {

        final String username = StringUtils.trimToNull(login);

         log.debug("Authenticating with %s".formatted(username) + " at time " + LocalDateTime.now());

        return userRepository.findByUsername(username)
                .flatMap(Mono::just)
                .map(AppUserPrinciple::new);
    }
}
