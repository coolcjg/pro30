package com.myspring.pro30.freeboard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.gallery.vo.GalleryVO;

public interface FreeController {
	
	//갤러리 리스트보기
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Criteria cri) throws Exception;
	
	//갤러리 새 글 쓰기
	public ResponseEntity add(GalleryVO galleryVO, MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
		
	
}
