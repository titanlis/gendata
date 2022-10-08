package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.shift.ShiftRepository;

import java.util.Calendar;

@Service
public class ShiftService {
    private static Logger logger = LoggerFactory.getLogger(ShiftService.class);
    private static ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public Integer getShiftId(Calendar calendar){
        return shiftRepository.findFirstByStartTimeBefore(calendar.getTime()).getId();
    }
}
