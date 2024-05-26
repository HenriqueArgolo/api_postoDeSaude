package argolo.tech.springsecurity6.controller.dto;

import java.time.Instant;
import java.util.List;

public record FeedDto(List<FeedTweetDto> feedTweet, int page, int pageSize, int totalPages, int totalElements) {
}
