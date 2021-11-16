package com.javacsvreader.service;

import com.javacsvreader.domain.SourceUserAnswer;
import com.javacsvreader.domain.UserVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserVisitService {

    private final UserVisitRepository repository;

    public Map<String, Integer> getUserCountPerSource() {
        return repository.countThemGroupBySource().stream()
                .collect(Collectors.toMap(
                        SourceUserAnswer::getSource,
                        answer -> answer.getCount().intValue()
                ));
    }
}
