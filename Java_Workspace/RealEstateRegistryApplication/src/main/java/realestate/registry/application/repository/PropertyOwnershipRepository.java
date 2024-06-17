package realestate.registry.application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import realestate.registry.application.beans.PropertyOwnership;

@Repository
public interface PropertyOwnershipRepository extends JpaRepository<PropertyOwnership, Integer>, PagingAndSortingRepository<PropertyOwnership, Integer>{ 

	@Query(nativeQuery = true,value="select * from property_ownership")
	public <T> Page<List<T>> findAllProperty(Pageable pageable, Class<T> type);
	
}
