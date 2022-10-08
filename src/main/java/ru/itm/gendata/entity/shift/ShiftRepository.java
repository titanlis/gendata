package ru.itm.gendata.entity.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    Shift findFirstByStartTimeBefore(Date now);
}