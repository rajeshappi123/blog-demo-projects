
package com.poc.mapqueuelock.repo;

import java.util.Optional;

import javax.persistence.LockModeType;

import com.poc.mapqueuelock.model.Request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RequestRepository extends PagingAndSortingRepository<Request, Integer> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    Page<Request> findAll(Pageable pageable);

    Request findByRefNumber(String refNumber);

    void deleteByRefNumber(String refNumber);
}