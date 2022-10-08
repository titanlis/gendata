package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.location.LocationRepository;
import java.util.LinkedList;
import java.util.List;

@Service
public class LocationService {
    private static Logger logger = LoggerFactory.getLogger(LocationService.class);
    private static LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Integer> getAllId(){
        List<Integer> listId = new LinkedList<>();
        locationRepository.findAll().stream().forEach(location -> {
            listId.add(location.getId());
        });
        return listId;
    }

}
