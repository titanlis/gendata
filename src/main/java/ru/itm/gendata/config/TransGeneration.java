package ru.itm.gendata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itm.gendata.components.TransCoordEntitiesGenerator;
import ru.itm.gendata.components.TransFuelEntityGenerator;

@Configuration
public class TransGeneration {
    @Bean
    public TransFuelEntityGenerator getTransFuelEntityGenerator(){
        return new TransFuelEntityGenerator();
    }

    @Bean
    public TransCoordEntitiesGenerator getTransCoordEntitiesGenerator(){
        return new TransCoordEntitiesGenerator();
    }
}
