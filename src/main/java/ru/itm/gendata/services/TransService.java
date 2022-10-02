package ru.itm.gendata.services;

import ru.itm.gendata.entity.trans.AbstractEntity;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TransService {
    protected LocalDateTime lastGeneration = LocalDateTime.now();
    protected static LocalDateTime lastSave = LocalDateTime.now();
    public LocalDateTime getLastGeneration() {
        return lastGeneration;
    }
    public static LocalDateTime getLastSave() {
        return lastSave;
    }
    public abstract Boolean isStarting();
    public abstract void start();
    public abstract void stop();
    public abstract String getData();
    public abstract AbstractEntity generate();

    public abstract void saveToBase(List<AbstractEntity> aE);

    public void setLastGeneration(LocalDateTime lastGeneration) {
        this.lastGeneration = lastGeneration;
    }

    public static void setLastSave(LocalDateTime lastSave) {
        TransService.lastSave = lastSave;
    }
}
