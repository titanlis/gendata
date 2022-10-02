package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.HoleRepository;
import ru.itm.gendata.entity.trans.TransKeysCycle;
import ru.itm.gendata.entity.trans.TransKeysDrilling;

import java.util.List;
import java.util.Random;

@Component
public class TransKeysDrillingGenerator implements AbstractEntitiesGenerator{
    private static Logger logger = LoggerFactory.getLogger(TransKeysDrillingGenerator.class);

    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;
    List<Long> listId = null;
    Long[] idTransSensor ={20082L, 35166L, 35200L, 35201L, 35489L, 35490L, 35491L, 35492L, 35493L, 35494L};
    private Random random = new Random();

    private HoleRepository holeRepository;

    @Autowired
    public TransKeysDrillingGenerator(HoleRepository holeRepository) {
        this.holeRepository = holeRepository;
    }

    @Override
    public AbstractEntity getEntity() {
        if(listId == null){
            listId = holeRepository.findAllId();
        }

        int listHolesSize = listId.size();
        if(listHolesSize>0){
            TransKeysDrilling transKeysDrilling = new TransKeysDrilling(
                    listId.get(random.nextInt(listHolesSize-1)),
                    1L,
                    idTransSensor[random.nextInt(9)]
            );
            return transKeysDrilling;
        }
        else{
            logger.warn("LIST_ID_HOLES not found.");
        }
        return null;
    }

    public void setGeneratorStart(Boolean generatorStart) {
        this.generatorStart = generatorStart;
    }

    public boolean getGeneratorStart() {
        return generatorStart;
    }


}
