package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryservice;
	
	@GetMapping("/categories")
	public String listCategories(Model model) {
		List<Category> categories = categoryservice.listAll();
		model.addAttribute("categories",categories);
		return "categories/categories.html";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> categories = categoryservice.listCategoriesUsedInForm();
		model.addAttribute("pageTitle","Create new category");
		model.addAttribute("category",new Category());
		model.addAttribute("categories",categories);
		
		return "categories/category_form.html";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category,
			RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		//Lay file name tu multipartfile object
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		//set filename vao category object
		category.setImage(fileName);
		
		//goi phuong thuc save cua service class de luu object vao db, va tra ve object da luu
		Category savedCategory = categoryservice.save(category);
		
		//Tao upload directory. vi directory nam cung level voi shopmebackend va frontend nen can su dung ../
		String uploadDir = "../category-images/" + savedCategory.getId();
		
		//Goi FileUploadUtil class de save file
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		//set succes message
		ra.addFlashAttribute("message", "The category " + savedCategory.getName() + " has been saved sucessfully!");
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		Category category = categoryservice.get(id);
		List<Category> listCategories = categoryservice.listCategoriesUsedInForm();
		
		model.addAttribute("category", category);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
		
		return "categories/category_form";
	}
	
	
}
