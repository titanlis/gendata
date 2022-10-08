package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.equipment.EquipmentLoadRepository;
import ru.itm.gendata.entity.operator.OperatorRepository;

import java.util.LinkedList;
import java.util.List;

@Service
public class OperatorService {
    private static Logger logger = LoggerFactory.getLogger(OperatorService.class);
    private static OperatorRepository operatorRepository;

    @Autowired
    public OperatorService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    public List<Short> getAllId() {
        List<Short> listId = new LinkedList<>();
        operatorRepository.findAll().stream().forEach(operator -> {
            listId.add(operator.getId());
        });
        return listId;
    }
}
