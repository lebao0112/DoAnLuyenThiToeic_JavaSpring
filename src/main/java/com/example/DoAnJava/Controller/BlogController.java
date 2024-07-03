package com.example.DoAnJava.Controller;

import com.example.DoAnJava.models.Blog;
import com.example.DoAnJava.services.BlogService;
import com.example.DoAnJava.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BlogController {

    @Autowired // Có thể sử dụng @Autowired hoặc constructor injection
    private final BlogService blogService;
    private final CategoryService categoryService;


    // Hiển thị chi tiết bài viết
    @GetMapping("/blogs/{id}")
    public String showBlogDetail(@PathVariable("id") Long id, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        model.addAttribute("blog", blog); // Changed to "blog" to match template
        return "blogs/blog-detail";
    }

    @GetMapping("/blogs")
    public String showBlogList(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "blogs/blogs-list";
    }

    @GetMapping("/blogs/add-blogs-list")
    public String showAddBlogList(Model model) {
        model.addAttribute("blog", blogService.getAllBlogs());
        return "blogs/add-blogs-list";
    }
    // Hiển thị form thêm mới blog
    @GetMapping("/blogs/add")
    public String showAddForm(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "blogs/add-blog";
    }

    // Xử lý thêm mới blog
    @PostMapping("/blogs/add")
    public String addBlog(@Valid Blog blog, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "blogs/add-blog";
        }
        blogService.addBlog(blog);
        return "redirect:/blogs";
    }



    // GET request để hiển thị form chỉnh sửa blog
    @GetMapping("/blogs/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        model.addAttribute("blogs", blog);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "blogs/update-blog";
    }

    // POST request để cập nhật blog
    @PostMapping("/blogs/update/{id}")
    public String updateBlog(@PathVariable("id") Long id, @Valid Blog blog,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            blog.setId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "blogs/update-blog";
        }
        blogService.updateBlog(blog);
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "redirect:/blogs";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteBlogById(id);
        return "redirect:/blogs";
    }
    // GET request để xóa blog
    @GetMapping("/blogs/delete/{id}")
    public String deleteBlog(@PathVariable("id") Long id, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        blogService.deleteBlogById(id);
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "redirect:/blogs";
    }
}
