package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    boolean existsByEmail(String email);
    List<User> findAllByIdIn(List<Long> ids);
    List<User> findAll();
}
