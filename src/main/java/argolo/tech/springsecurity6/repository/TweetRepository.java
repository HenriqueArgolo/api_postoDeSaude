package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TweetRepository extends JpaRepository<Tweet, UUID> {
}
