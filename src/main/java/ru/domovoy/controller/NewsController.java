package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.News;
import ru.domovoy.service.NewsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/news")
@CrossOrigin(origins = "*")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> news = newsService.getAllNews();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/published")
    public ResponseEntity<List<News>> getPublishedNews() {
        List<News> news = newsService.getPublishedNews();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable @NonNull Long id) {
        Optional<News> news = newsService.getNewsById(id);
        return news.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody @NonNull News news) {
        News createdNews = newsService.createNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable @NonNull Long id, @RequestBody @NonNull News news) {
        try {
            News updatedNews = newsService.updateNews(id, news);
            return ResponseEntity.ok(updatedNews);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable @NonNull Long id) {
        try {
            newsService.deleteNews(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}



