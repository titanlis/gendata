package ru.itm.gendata.components;

import ru.itm.gendata.entity.trans.AbstractEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public interface AbstractEntitiesGenerator {
    AbstractEntity getEntity();

    /**
     * Возвращает время начала смены. 9:00
     * Если сейчас, например 8 часов, то значит смена началась в 9:00, но вчера.
     * Если сейчас 14:00, то значит смена началась сегодня в 9:00
     * @return начало смены
     */
    default Calendar getStartShift() {
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) < 7 || (calendar.get(Calendar.HOUR_OF_DAY)==7 && calendar.get(Calendar.MINUTE)<30)){
            calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-1);
        }
        else{
            calendar.set(Calendar.HOUR_OF_DAY,0);
        }

        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar;
    }

    /**
     *
     * @return Дата начала смены
     */
    default LocalDate getStartShiftDate(){
        return LocalDate.ofInstant(getStartShift().toInstant(), ZoneId.systemDefault());
    }


}
