package ru.itm.gendata.entity.trans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransRefuelRepository extends JpaRepository<TransRefuel, Integer> {
    List<TransRefuel> findAllByEndDtIsNull();
}