package org.example.homeservice.repository.baseentity;

import org.example.homeservice.entites.BaseEntity;
import org.example.homeservice.entites.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
@Primary
@Repository
public interface BaseEnitityRepo<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

   // List<T> findWithAttribute(String attributeName, Object attributeValue);
//    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM #{#entityName} e WHERE e.:attributeName = :attributeValue")
//    boolean existsByAttribute(@Param("attributeName") String attributeName, @Param("attributeValue") Object attributeValue);

   // boolean existsByAttribute(String attributeName, Object attributeValue);
}
