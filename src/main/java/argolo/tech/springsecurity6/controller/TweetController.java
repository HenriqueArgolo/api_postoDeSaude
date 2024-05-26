package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.controller.dto.TweetDto;
import argolo.tech.springsecurity6.entities.Tweet;
import argolo.tech.springsecurity6.repository.TweetRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequestMapping("/api")
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    public TweetController(TweetRepository tweet, UserRepository userRepository) {
        this.tweetRepository = tweet;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> addTweet(@RequestBody TweetDto tweetdto, JwtAuthenticationToken userToken) {
        var user = userRepository.findById(UUID.fromString(userToken.getName()));
        var tweet = new Tweet();

        user.ifPresent(tweet::setUser);
        tweet.setContent(tweetdto.content());
        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }
}
