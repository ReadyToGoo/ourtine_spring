package ourtine.repository;

import ourtine.domain.Hashtag;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag,Long> {


    Optional<Hashtag> findHashtagByName(String name);


}
