package realestate.registry.application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import realestate.registry.application.beans.RegistryUser;

@Repository
public interface RegistryUserRepository extends JpaRepository<RegistryUser, Integer>, PagingAndSortingRepository<RegistryUser, Integer>{ 

	@Query(nativeQuery = true,value="select * from registry_user")
	public <T> Page<List<T>> findAllRegistryUsers(Pageable pageable, Class<T> type);
	
	@Query(value="SELECT user FROM RegistryUser user WHERE user.username = :userName AND user.activeFlag = true")
	public RegistryUser findByUserName(String userName);
	
	@Query(value="SELECT user FROM RegistryUser user WHERE user.isLoggedIn = true AND user.activeFlag = true")
	public List<RegistryUser> findLoggedInUsers();
	
	@Query(value="SELECT user FROM RegistryUser user WHERE user.aadharNumber = :aadharNumber AND user.activeFlag = true")
	public RegistryUser findUserByAadharnumber(Long aadharNumber);

	@Query(value="SELECT user FROM RegistryUser user WHERE user.isRegistrar = true AND user.activeFlag = true")
	public List<RegistryUser> findRegistrars();
}
