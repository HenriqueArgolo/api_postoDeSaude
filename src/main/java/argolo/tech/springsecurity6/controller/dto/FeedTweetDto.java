package argolo.tech.springsecurity6.controller.dto;

import java.time.Instant;

public record FeedTweetDto(Long tweetId, String content, String username, Instant postTime) {
}
