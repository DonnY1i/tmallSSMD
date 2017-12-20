package com.donny1i.tmall.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.donny1i.tmall.pojo.Category;
import com.donny1i.tmall.service.CategoryService;
import com.donny1i.tmall.util.ImageUtil;
import com.donny1i.tmall.util.Page;
import com.donny1i.tmall.util.UploadedImageFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@RequestMapping("admin_category_list")
	public String list(Model model, Page page){
		PageHelper.offsetPage(page.getStart(), page.getCount());
		List<Category> cs = categoryService.list();
		int total = (int) new PageInfo<>(cs).getTotal();
		page.setTotal(total);
		model.addAttribute("page", page);
		model.addAttribute("cs", cs);
		return "admin/listCategory";
	}
	
	@RequestMapping("admin_category_add")
	public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
		System.out.println(session.getServletContext().getRealPath("img/category"));
		categoryService.add(c);
		File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, c.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		uploadedImageFile.getImage().transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);
		ImageIO.write(img, "jpg", file);
		return "redirect:/admin_category_list";
	}
	
	@RequestMapping("admin_category_delete")
	public String delete(int id, HttpSession session) throws IOException{
		categoryService.delete(id);
		
		File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, id+".jpg");
		file.delete();
		
		return "redirect:/admin_category_list";
	}
	
	@RequestMapping("admin_category_edit")
	public String edit(int id,Model model)throws IOException{
		Category c = categoryService.get(id);
		model.addAttribute("c", c);
		return "admin/editCategory";
	}
	
	@RequestMapping("admin_category_update")
	public String update(Category category, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException{
		categoryService.update(category);
		MultipartFile image = uploadedImageFile.getImage();
		if(null != image && !image.isEmpty()){
			File imageFolder = new File(session.getServletContext().getRealPath("image/category"));
			File file = new File(imageFolder,category.getId()+".jpg");
			image.transferTo(file);
			BufferedImage img = ImageUtil.change2jpg(file);
			ImageIO.write(img, "jpg", file);
		}
		return "redirect:/admin_category_list";
	}
}
