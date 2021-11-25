package com.tw.userservice.service;

import com.tw.userservice.model.Level;
import com.tw.userservice.model.Request;
import com.tw.userservice.model.Task;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification implements Specification<Task> {

    private final Request request;

    public TaskSpecification(Request request) {
        this.request = request;
    }


    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> list = new ArrayList<>();
        list.add(criteriaBuilder.equal(root.get("status"),true));

       if(request.getUserId()!=null)
       {
           list.add(criteriaBuilder.equal(root.get("userId"),request.getUserId()));
       }
        if(request.getLevel()!=null) {
            list.add(criteriaBuilder.equal(root.get("level"), request.getLevel()));
        }
      //  list.add(criteriaBuilder.equal(root.get("description"),"创建数据库"));
        Predicate[] arr = new Predicate[list.size()];
        return criteriaBuilder.and(list.toArray(arr));
       // Predicate pre = criteriaBuilder.equal(root.get("userId"),"0319349339968");
    }
}
