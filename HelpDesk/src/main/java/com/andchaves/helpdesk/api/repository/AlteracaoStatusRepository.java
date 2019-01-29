package com.andchaves.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.andchaves.helpdesk.api.entity.AlteracaoStatus;

public interface AlteracaoStatusRepository extends MongoRepository<AlteracaoStatus, String>{

	Iterable<AlteracaoStatus> findByTicketIdOrderByDateAlteracaoStatusDesc(String ticketId);
}
