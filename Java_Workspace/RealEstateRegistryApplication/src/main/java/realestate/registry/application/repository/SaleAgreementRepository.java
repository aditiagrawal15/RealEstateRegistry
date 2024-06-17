package realestate.registry.application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import realestate.registry.application.beans.SaleAgreement;

@Repository
public interface SaleAgreementRepository extends JpaRepository<SaleAgreement, Integer>, PagingAndSortingRepository<SaleAgreement, Integer>{ 

	@Query(nativeQuery = true,value="select * from sale_agreement")
	public <T> Page<List<T>> findAllSaleAgreements(Pageable pageable, Class<T> type);
	
	@Query(nativeQuery = true,value="SELECT * FROM sale_agreement WHERE property_fk = :propertyId AND active_flag = true")
	public SaleAgreement findByPropertyId(int propertyId);
	
	@Query(nativeQuery = true,value="SELECT * FROM sale_agreement WHERE agreement_id = :Id")
	public SaleAgreement findByIdandActiveFlag(int Id);
	
}
