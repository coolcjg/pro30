package com.myspring.pro30.gallery.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.Criteria;

public interface GalleryController {
	

	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Criteria cri) throws Exception;
	/*
	public ResponseEntity addNewArticle(ArticleVO articleVO, MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, @ModelAttribute("Criteria") Criteria cri, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	*/
	

}
