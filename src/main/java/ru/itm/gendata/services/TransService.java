package ru.itm.gendata.services;

import ru.itm.gendata.entity.trans.AbstractEntity;

public interface TransService {
    Boolean isStarting();
    void start();
    void stop();
    String getData();
}
