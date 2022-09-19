package ru.itm.gendata.entity.equipment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Long> {
    EquipmentType findByIdIs(Long id);
}