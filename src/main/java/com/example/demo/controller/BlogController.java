package com.example.demo.controller;

import com.example.demo.model.Blog;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;
    private final UserRepository userRepository;

    public BlogController(BlogService blogService, UserRepository userRepository) {
        this.blogService = blogService; this.userRepository = userRepository;
    }

    @GetMapping
    public List<Blog> all() { return blogService.findAll(); }

    @GetMapping("/me")
    public ResponseEntity<?> myBlogs() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(blogService.findByAuthorUsername(username));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Blog blog) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User u = userRepository.findByUsername(username).orElseThrow();
        blog.setAuthor(u);
        Blog saved = blogService.save(blog);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Blog input) {
        Blog exist = blogService.findById(id).orElse(null);
        if (exist == null) return ResponseEntity.notFound().build();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!exist.getAuthor().getUsername().equals(username) && !isAdmin) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        exist.setTitle(input.getTitle());
        exist.setContent(input.getContent());
        blogService.save(exist);
        return ResponseEntity.ok(exist);
    }

    // only ADMIN can delete any blog. (adjust if you want owner delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!blogService.findById(id).isPresent()) return ResponseEntity.notFound().build();
        blogService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
