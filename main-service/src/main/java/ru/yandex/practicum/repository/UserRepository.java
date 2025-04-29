package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.model.User;
import ru.yandex.practicum.dto.admin.user.AdminUserDto;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    List<AdminUserDto> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
