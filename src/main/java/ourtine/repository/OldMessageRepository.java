package ourtine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ourtine.domain.OldMessage;

@Repository
public interface OldMessageRepository extends JpaRepository<OldMessage, Long> {

    //Slice<OldMessage> findFirst30ByReceiverOrderByCreatedAtDesc(User Receiver);
}
