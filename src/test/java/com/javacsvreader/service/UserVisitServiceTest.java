package com.javacsvreader.service;

import com.javacsvreader.domain.SourceUserAnswer;
import com.javacsvreader.domain.UserVisitRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserVisitServiceTest {

    @Mock
    private UserVisitRepository userVisitRepository;

    @InjectMocks
    private UserVisitService userVisitService;

    @Test
    void getUserCountPerSourceWithData_worksOk() {
        // Given
        String source = "test1";
        Long count = 3L;
        List<SourceUserAnswer> sourceUsers = List.of(
                new SourceUserAnswer(source, count)
        );
        when(userVisitRepository.countThemGroupBySource()).thenReturn(sourceUsers);
        // When
        Map<String, Integer> countPerSource = userVisitService.getUserCountPerSource();
        // Then
        assertEquals(1, countPerSource.size());
        assert countPerSource.keySet().stream().findFirst().isPresent();
        assertEquals(source, countPerSource.keySet().stream().findFirst().get());
        assertEquals(count.intValue(), countPerSource.get(source));
    }

    @Test
    void getUserCountPerSourceWithNoData_returnsEmptyList() {
        // Given
        List<SourceUserAnswer> sourceUsers = List.of();
        when(userVisitRepository.countThemGroupBySource()).thenReturn(sourceUsers);
        // When
        Map<String, Integer> countPerSource = userVisitService.getUserCountPerSource();
        // Then
        assert countPerSource.isEmpty();
    }

}
