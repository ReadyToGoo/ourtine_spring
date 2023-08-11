package ourtine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ourtine.domain.NewMessage;
import ourtine.domain.User;

import java.util.List;

@Repository
public interface NewMessageRepository extends JpaRepository<NewMessage, Long> {

    Slice<NewMessage> findByReceiver(User receiver, Pageable pageable);
}
