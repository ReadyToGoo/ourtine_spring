package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ourtine.domain.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long> {
    // 참여자 중에 차단한 유저가 있는지 여부
}
