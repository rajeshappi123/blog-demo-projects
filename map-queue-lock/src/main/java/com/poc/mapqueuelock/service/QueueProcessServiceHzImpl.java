package com.poc.mapqueuelock.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.hazelcast.core.HazelcastInstance;
import com.poc.mapqueuelock.model.Request;
import com.poc.mapqueuelock.repo.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class QueueProcessServiceHzImpl implements QueueProcessService {

    private final RequestRepository repo;

    private final HazelcastInstance hzInstance;

    private Map<String, String> lockMap = null;

    public QueueProcessServiceHzImpl(@Autowired final RequestRepository repo, @Autowired HazelcastInstance hzInstance) {
        this.repo = repo;
        this.hzInstance = hzInstance;
        this.lockMap = hzInstance.getMap("recordsInReview");
    }
    

    protected Map<String, String> lockMap() {
        log.debug("lockMap impl->{}", lockMap.getClass());
        return lockMap;
    }

    protected Optional<String> getFromLockMap(final String userId) {

        String refNumber = null;

        final Map<String, String> lockMap = this.lockMap();
        synchronized (lockMap) {
            final List<String> existingIds = lockMap.entrySet().stream()
                    .filter(entry -> userId.equals(entry.getValue())).map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (!existingIds.isEmpty()) {
                refNumber = existingIds.iterator().next();
            }
        }
        log.debug("getFromLockMap: refNumber-{} for userId->{}", refNumber, userId);
        return Optional.ofNullable(refNumber);
    }

    @Override
    public Optional<Request> getNextAvailable(final String userId) throws QueueProcessServiceException {

        try {
            Request result = null;
            int pageNumber = 0;

            final Map<String, String> lockMap = this.lockMap();

            synchronized (lockMap) {

                // check if this user already have a reuest locked for review
                final Optional<String> existingRefNumber = this.getFromLockMap(userId);
                if (existingRefNumber.isPresent()) {
                    result = this.repo.findByRefNumber(existingRefNumber.get());
                    if (result != null) {
                        return Optional.of(result);
                    }
                }

                // otherwise - find next available
                do {

                    final Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("id").ascending());
                    final Page<Request> page = this.repo.findAll(pageable);
                    final List<Request> pageRequests = page.getContent();

                    // queue is empty
                    if (pageRequests.isEmpty()) {
                        return Optional.ofNullable(null);
                    }

                    // check items from this page
                    for (int i = 0; i < pageRequests.size(); i++) {
                        result = pageRequests.get(i);

                        final String refNumber = result.getRefNumber();
                        final String marker = lockMap.get(refNumber);
                        log.debug("getNextAvailable: marker->{} for refNumber-{} and userId->{}", marker, refNumber,
                                userId);

                        if (marker == null) {
                            lockMap.put(refNumber, userId);
                            log.debug("getNextAvailable: lockMap#locked refNumber-{} and userId->{}", marker, refNumber,
                                    userId);
                            break;
                        } else {
                            result = null;
                        }
                    }

                    // will try next page
                    if (result == null) {
                        log.debug("getNextAvailable: result is null for userId->{} and pageNumber->{}", userId,
                                pageNumber);
                        pageNumber++;
                    }

                } while (result == null);
            }

            log.debug("getNextAvailable: final result-> {}", result);
            return Optional.of(result);
        } catch (Exception e) {
            throw new QueueProcessServiceException(e);
        }
    }

    @Override
    public void markAsFulfilled(final String refNumber) throws QueueProcessServiceException {

        final Map<String, String> lockMap = this.lockMap();
        synchronized (lockMap) {
            if (lockMap.containsKey(refNumber)) {

                this.repo.deleteByRefNumber(refNumber);
                log.debug("markAsFulfilled: deleteByRefNumber-> {}", refNumber);

                lockMap.remove(refNumber);
                log.debug("markAsFulfilled: lockMap#remove-> {}", refNumber);
            }
        }
    }
}