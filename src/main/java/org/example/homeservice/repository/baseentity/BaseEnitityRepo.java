package org.example.homeservice.repository.baseentity;

import org.example.homeservice.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

//@Primary
//@Repository
public interface BaseEnitityRepo<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

   // List<T> findWithAttribute(String attributeName, Object attributeValue);
//    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM #{#entityName} e WHERE e.:attributeName = :attributeValue")
//    boolean existsByAttribute(@Param("attributeName") String attributeName, @Param("attributeValue") Object attributeValue);

   // boolean existsByAttribute(String attributeName, Object attributeValue);
}
