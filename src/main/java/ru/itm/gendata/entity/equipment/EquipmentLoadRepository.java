package ru.itm.gendata.entity.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentLoadRepository extends JpaRepository<EquipmentLoad, Short> {
}