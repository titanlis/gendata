package ru.itm.gendata.entity.trans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface HoleRepository extends JpaRepository<Hole, Long> {
    List<Hole> findAll();
    default List<Long> findAllId(){
        List<Long> list = new ArrayList<>();
        findAll().stream().forEach(l->{
            list.add(l.getId());
        });
        return list;
    }
}