package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.shift.Shift;
import ru.itm.gendata.entity.shift.ShiftRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ShiftService {
    private static Logger logger = LoggerFactory.getLogger(ShiftService.class);
    private static ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public Integer getShiftId(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date dateStart = sdf.parse("07:30:00");
            Date dateEnd = sdf.parse("19:30:00");
            if(calendar.getTime().after(dateStart) && calendar.getTime().before(dateEnd)){
                return 1;
            }
            else{
                return 2;
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
