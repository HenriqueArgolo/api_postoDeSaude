package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.controller.dto.FeedDto;
import argolo.tech.springsecurity6.controller.dto.FeedTweetDto;
import argolo.tech.springsecurity6.controller.dto.TweetDto;
import argolo.tech.springsecurity6.entities.Role;
import argolo.tech.springsecurity6.entities.Tweet;
import argolo.tech.springsecurity6.repository.TweetRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0" ) int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        var tweets = tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimesTamp" ))
                .map(tweet -> new FeedTweetDto(tweet.getId(), tweet.getContent(), tweet.getUser().getUserName(), tweet.getCreationTimesTamp()));

        return ResponseEntity.ok(new FeedDto(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getNumberOfElements()));
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


    @DeleteMapping("/tweet/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found"));


        var isAdmin = user.get().getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || tweet.getUser().getId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.delete(tweet);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
}
