package com.javacsvreader;

import com.javacsvreader.domain.UserVisitRepository;
import com.javacsvreader.dto.FileRequestDTO;
import com.javacsvreader.reader.CSVReader;
import com.javacsvreader.reader.FileProcessedStatus;
import com.javacsvreader.service.UserVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class Controller {

    private static final int FILE_PROCESSING_TIMEOUT = 2_000;

    private final CSVReader csvReader;
    private final UserVisitRepository repository;
    private final UserVisitService userVisitService;

    @PostMapping("/processFile")
    public String processFile(@RequestBody FileRequestDTO requestDTO) {

        FileProcessedStatus status = csvReader.processWithTimeout(requestDTO.getAbsolutePath());
        switch (status) {
            case DONE: return "File was processed successfully, Last user count gives: " + countSourceUsers();
            case FAILED: return "An error was produced processing the file, check the logs for further information";
            case IN_PROGRESS:
            default: return "The file is taking over " + CSVReader.FILE_PROCESSING_TIMEOUT
                    + " milliseconds to load, but we're on it";
        }
    }

    @GetMapping("/userCount")
    public Map<String, Integer> countSourceUsers() {
        return userVisitService.getUserCountPerSource();
    }

}
