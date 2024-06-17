package realestate.registry.application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import realestate.registry.application.beans.RegistryNagarpalikaRecord;

@Repository
public interface RegistryNagarpailkaRecordsRepository extends JpaRepository<RegistryNagarpalikaRecord, Integer>, PagingAndSortingRepository<RegistryNagarpalikaRecord, Integer>{ 

	@Query(nativeQuery = true,value="select * from registry_nagarpalika")
	public <T> Page<List<T>> findAllRegistryNagarpalikaRecords(Pageable pageable, Class<T> type);
	
	@Query(nativeQuery = true,value="SELECT * FROM registry_nagarpalika WHERE owner_fk = :userId")
	public List<RegistryNagarpalikaRecord> findByUserId(int userId);
	
}
