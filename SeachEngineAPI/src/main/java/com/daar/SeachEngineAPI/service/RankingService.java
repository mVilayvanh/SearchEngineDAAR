package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    private final PreprocessingService preprocessingService;

    public RankingService(PreprocessingService preprocessingService) {
        this.preprocessingService = preprocessingService;
    }

    public List<Book> ClosenessRanking(List<Book> books) {
        return books.stream()
                .sorted((a, b) -> Double.compare(
                        preprocessingService.getCloseness(b.getId()),
                        preprocessingService.getCloseness(a.getId())
                ))
                .toList();
    }
}
