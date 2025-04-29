package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.mappers.UserMapper;
import ru.yandex.practicum.dao.model.User;
import ru.yandex.practicum.dto.admin.user.AdminCreateUserDto;
import ru.yandex.practicum.dto.admin.user.AdminUserDto;
import ru.yandex.practicum.exception.DataErrorException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.UniqueException;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<AdminUserDto> getList(int from, int size, List<Long> ids) {
        Pageable pageable = PageRequest.of(from / size, size);

        return ids == null || ids.isEmpty()
                ? userRepository.findAll(pageable).stream().map(UserMapper::toAdminDto).toList()
                : userRepository.findAllByIdIn(ids, pageable);
    }

    @Override
    public AdminUserDto create(AdminCreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UniqueException("User with email " + dto.getEmail() + " already exists");
        }

        User user = UserMapper.toEntity(dto);

        try {
            user = userRepository.save(user);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return UserMapper.toAdminDto(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id " + id + " does not exist");
        }

        try {
            userRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }
    }
}
