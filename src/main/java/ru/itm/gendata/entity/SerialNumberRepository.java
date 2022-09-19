package ru.itm.gendata.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerialNumberRepository extends JpaRepository<SerialNumber, Long> {
}