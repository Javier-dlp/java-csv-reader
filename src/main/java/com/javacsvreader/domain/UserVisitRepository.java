package com.javacsvreader.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserVisitRepository extends CrudRepository<UserVisit, UserVisitId> {

    @Query("select new com.javacsvreader.domain.SourceUserAnswer(source, count(*)) from UserVisit group by source")
    List<SourceUserAnswer> countThemGroupBySource();

}
