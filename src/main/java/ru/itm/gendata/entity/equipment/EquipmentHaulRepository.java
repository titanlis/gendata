package ru.itm.gendata.entity.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentHaulRepository extends JpaRepository<EquipmentHaul, Long> {

}