package ru.itm.gendata.entity.trans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransFuelRepository extends JpaRepository<TransFuel, Long> {
    Optional<TransFuel> findFirstByEquipIdEquals(Long idEquip);
}