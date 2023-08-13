package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ourtine.domain.OldMessage;
import ourtine.domain.User;


@Repository
public interface OldMessageRepository extends JpaRepository<OldMessage, Long> {

    //Slice<OldMessage> findFirst30ByReceiverOrderByCreatedAtDesc(User Receiver);
}
