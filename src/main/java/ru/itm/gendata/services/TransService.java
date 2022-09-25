package ru.itm.gendata.services;

import ru.itm.gendata.entity.trans.AbstractEntity;

public interface TransService {
    void pause(long l);

    Boolean isStarting();

    void start();
    void saveToBase(AbstractEntity a);

    void stop();
}
