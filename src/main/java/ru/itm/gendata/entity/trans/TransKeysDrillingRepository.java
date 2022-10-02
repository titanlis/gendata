package ru.itm.gendata.entity.trans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransKeysDrillingRepository extends JpaRepository<TransKeysDrilling, Long> {
}