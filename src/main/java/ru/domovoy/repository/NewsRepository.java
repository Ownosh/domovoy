package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.News;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByAuthorUserId(Long authorId);
    List<News> findByIsPublished(Boolean isPublished);
    List<News> findByType(News.NewsType type);
}



