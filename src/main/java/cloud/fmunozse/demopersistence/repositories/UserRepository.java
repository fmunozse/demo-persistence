package cloud.fmunozse.demopersistence.repositories;

import cloud.fmunozse.demopersistence.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User,Long> {

}
