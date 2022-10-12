package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.equipment.EQUIPMENT_TYPE;
import ru.itm.gendata.entity.equipment.Equipment;
import ru.itm.gendata.entity.equipment.EquipmentRepository;
import ru.itm.gendata.entity.location.LocationRepository;

import java.util.*;

@Service
public class EquipmentService  {
    private static Logger logger = LoggerFactory.getLogger(EquipmentService.class);
    private static EquipmentRepository equipmentRepository;
    private EquipmentTypeService equipmentTypeService;
    private Random random = new Random();

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository, EquipmentTypeService equipmentTypeService) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentTypeService = equipmentTypeService;
    }

    public List<Equipment> getAllEquipment(){
        return equipmentRepository.findAll();
    }

    public List<Long> getAllId(){
        List<Long> listId = new LinkedList<>();
        equipmentRepository.findAll().stream().forEach(equipment -> {
            listId.add(equipment.getId());
        });
        return listId;
    }


    public Long getRandomId(){
        List<Long> listId = getAllId();
        return listId.get(random.nextInt(listId.size()-1));
    }

    public Long getRandomIdNotFuelTruck(Long defId){
        List<Long> listId = new ArrayList<>();
        if(listId==null || listId.isEmpty()) return defId;

        getAllEquipment().stream()
                .filter(equipment -> equipmentTypeService.getEquipmentType(equipment)!=null && equipmentTypeService.getEquipmentType(equipment)!= EQUIPMENT_TYPE.FUELTRUCK)
                .distinct()
                .forEach(equipment -> listId.add(equipment.getId()));
        return listId.get(random.nextInt(listId.size()-1));
    }


}
