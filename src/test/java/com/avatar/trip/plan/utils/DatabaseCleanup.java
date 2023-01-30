package com.avatar.trip.plan.utils;

import static com.avatar.trip.plan.AcceptanceTest.ADMIN_EMAIL;
import static com.avatar.trip.plan.AcceptanceTest.ADMIN_PASSWORD;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.user.domain.User;
import com.avatar.trip.plan.user.domain.UserAuthority;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles(profiles = "test")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder encoder;

    private List<String> tables;

    @Override
    public void afterPropertiesSet() {
        tables = entityManager.getMetamodel().getEntities().stream()
            .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
            .map(e -> camelToLowerUnderscore(e.getName()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void execute(){
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tables) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager
                .createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1")
                .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void saveInitData(){

        //관리자 권한 생성
        Authority roleAdmin = Authority.from(Role.ADMIN);
        entityManager.persist(roleAdmin);

        //관리자 생성
        String encodePassword = encoder.encode(ADMIN_PASSWORD);
        User admin = User.of(ADMIN_EMAIL, encodePassword, List.of(UserAuthority.of(roleAdmin)));
        entityManager.persist(admin);

        entityManager.flush();
    }

    private String camelToLowerUnderscore(String s){
        String regex = "([a-z])([A-Z]+)";
        String replace = "$1_$2";
        return s.replaceAll(regex, replace).toLowerCase();
    }
}
