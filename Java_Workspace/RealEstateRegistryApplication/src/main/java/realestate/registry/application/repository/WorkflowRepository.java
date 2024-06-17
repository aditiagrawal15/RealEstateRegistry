package realestate.registry.application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import realestate.registry.application.beans.WorkFlow;

@Repository
public interface WorkflowRepository extends JpaRepository<WorkFlow, Integer>, PagingAndSortingRepository<WorkFlow, Integer>{ 

	@Query(nativeQuery = true,value="select * from workflow WHERE active_flag = true")
	public <T> Page<List<T>> findAllSaleAgreements(Pageable pageable, Class<T> type);
	
	@Query(value="SELECT w FROM WorkFlow w WHERE (w.sellerApprover.id=:userId OR registrarApprover.id=:userId OR raisedBy.id=:userId) AND activeFlag = true")
	public <T> List<T> findWorkFlowsByUserId(Integer userId, Class<T> type);
	
	
	@Query(nativeQuery = true,value="SELECT * FROM workflow WHERE id=:workflowId AND active_flag = true")
	public WorkFlow findByIdandActiveFlag(Long workflowId);
	
}
