package ru.itm.gendata.entity.equipment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Equipment findFirstByMtSn(String mtSn);
}