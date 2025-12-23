package ru.domovoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domovoy.model.News;
import ru.domovoy.repository.NewsRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public List<News> getPublishedNews() {
        return newsRepository.findByIsPublished(true);
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public News createNews(News news) {
        return newsRepository.save(news);
    }

    public News updateNews(Long id, News newsDetails) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        
        news.setTitle(newsDetails.getTitle());
        news.setContent(newsDetails.getContent());
        news.setType(newsDetails.getType());
        news.setImageUrl(newsDetails.getImageUrl());
        news.setIsPublished(newsDetails.getIsPublished());
        
        return newsRepository.save(news);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}











