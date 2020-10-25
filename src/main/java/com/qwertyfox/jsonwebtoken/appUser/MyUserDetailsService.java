package com.qwertyfox.jsonwebtoken.appUser;

import com.qwertyfox.jsonwebtoken.dao.MyUser;
import com.qwertyfox.jsonwebtoken.dao.MyUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("myUDS")
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserDao myUserDao;

    @Autowired
    public MyUserDetailsService(MyUserDao myUserDao) {
        this.myUserDao = myUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MyUser> myUser = myUserDao.findByUsername(username);
        myUser.orElseThrow( () -> new UsernameNotFoundException("User " +username+ " is not in the database"));

        return new PrincipleUser(myUser.get());
    }
}
