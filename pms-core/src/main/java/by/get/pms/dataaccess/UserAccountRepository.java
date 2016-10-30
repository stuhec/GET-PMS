package by.get.pms.dataaccess;

import by.get.pms.model.User;
import by.get.pms.model.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by milos on 12-Oct-16.
 */
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

    // @Query("select ua from UserAccount ua where ua.user = :user")
    UserAccount findUserAccountByUser(@Param("user") User user);

    @Query("select ua from UserAccount ua where UPPER(ua.userName) = UPPER(:userName)")
    UserAccount findUserAccountByUsername(@Param("userName") String userName);

    @Query("select ua from UserAccount ua where UPPER(ua.userName) = UPPER(:userName) and ua.active='Y'")
    UserAccount findActiveUserAccountByUsername(@Param("userName") String userName);
}
