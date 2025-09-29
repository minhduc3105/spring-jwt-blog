package com.example.demo.service;

import com.example.demo.model.Blog;
import com.example.demo.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    public BlogService(BlogRepository blogRepository) { this.blogRepository = blogRepository; }

    public Blog save(Blog b) { return blogRepository.save(b); }

    public List<Blog> findAll() { return blogRepository.findAll(); }

    public Optional<Blog> findById(Long id) { return blogRepository.findById(id); }

    public void deleteById(Long id) { blogRepository.deleteById(id); }

    public List<Blog> findByAuthorUsername(String username) { return blogRepository.findByAuthorUsername(username); }
}
