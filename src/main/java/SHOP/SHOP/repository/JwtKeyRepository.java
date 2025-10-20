package SHOP.SHOP.repository;

import SHOP.SHOP.model.jwt_keys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtKeyRepository extends JpaRepository<jwt_keys, Long>{
    @Query(value = "SELECT key_value FROM jwt_keys ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    String findLatestKey();
}
