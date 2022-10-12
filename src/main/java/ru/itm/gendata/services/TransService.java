package ru.itm.gendata.services;

import ru.itm.gendata.entity.trans.AbstractEntity;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TransService {

    public abstract LocalDateTime getLastGeneration();
    public abstract LocalDateTime getLastSave();

    public abstract Boolean isStarting();
    public abstract void start();
    public abstract void stop();
    public abstract String getData();
    public abstract AbstractEntity generate();
    public abstract void saveToBase(List<AbstractEntity> aE);
    public abstract void saveOne(AbstractEntity abstractEntity);
    public abstract String getName();
}
