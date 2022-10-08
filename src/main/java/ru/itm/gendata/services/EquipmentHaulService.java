package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.equipment.EquipmentHaulRepository;
import java.util.LinkedList;
import java.util.List;

@Service
public class EquipmentHaulService {
    private static Logger logger = LoggerFactory.getLogger(EquipmentHaulService.class);
    private static EquipmentHaulRepository equipmentHaulRepository;

    @Autowired
    public EquipmentHaulService(EquipmentHaulRepository equipmentHaulRepository) {
        this.equipmentHaulRepository = equipmentHaulRepository;
    }

    /**
     *
     * @return все id из equipHoul
     */
    public List<Short> getAllId(){
        List<Short> listId = new LinkedList<>();
        equipmentHaulRepository.findAll().stream().forEach(equipmentHaul -> {
            listId.add(equipmentHaul.getEquip_id());
        });
        return listId;
    }
}
