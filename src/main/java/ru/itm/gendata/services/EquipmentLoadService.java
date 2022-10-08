package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.equipment.EquipmentLoadRepository;
import java.util.LinkedList;
import java.util.List;

@Service
public class EquipmentLoadService {
    private static Logger logger = LoggerFactory.getLogger(EquipmentLoadService.class);
    private static EquipmentLoadRepository equipmentLoadRepository;

    @Autowired
    public EquipmentLoadService(EquipmentLoadRepository equipmentLoadRepository){
        this.equipmentLoadRepository = equipmentLoadRepository;
    }

    /**
     *
     * @return все id из equipLoad
     */
    public List<Short> getAllId(){
        List<Short> listId = new LinkedList<>();
        equipmentLoadRepository.findAll().stream().forEach(equipment -> {
            listId.add(equipment.getEquip_id());
        });
        return listId;
    }
}