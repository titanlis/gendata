package ru.itm.gendata.entity.trans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransCycleRepository extends JpaRepository<TransCycle, Integer> {
    List<TransCycle> findAllByEndTimeIsNull();
}