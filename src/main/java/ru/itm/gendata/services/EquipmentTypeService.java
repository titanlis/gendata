package ru.itm.gendata.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.equipment.EQUIPMENT_TYPE;
import ru.itm.gendata.entity.equipment.Equipment;
import ru.itm.gendata.entity.equipment.EquipmentType;
import ru.itm.gendata.entity.equipment.EquipmentTypeRepository;

import java.util.Optional;

@Service
public class EquipmentTypeService {
    private static EquipmentTypeRepository equipmentTypeRepository;

    @Autowired
    public EquipmentTypeService(EquipmentTypeRepository equipmentTypeRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
    }

    public EQUIPMENT_TYPE getEquipmentType(Equipment equipment){
        Optional<EquipmentType> optionalEQUIPMENT_type = equipmentTypeRepository.findById(equipment.getEquip_type_id());
        if(optionalEQUIPMENT_type.isPresent()){
            return optionalEQUIPMENT_type.get().getName();
        }
        return null;
    }
}
