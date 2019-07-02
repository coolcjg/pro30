package com.myspring.pro30.gallery.service;

import java.util.List;
import java.util.Map;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.gallery.vo.GalleryVO;


public interface GalleryService {
	
	
	
	public List<ArticleVO> list(Criteria cri) throws Exception;
	public int serviceGetTotal(Criteria cri);
	public void add(Map articleMap, GalleryVO galleryVO) throws Exception;
	public BoardAttachVO thumbnail(int articleNO) throws Exception;
	
	/*
	public int addNewArticle(Map articleMap) throws Exception;
	
	
	public ArticleVO viewArticle(int articleNO) throws Exception;
	
	public void modArticle(Map articleMap) throws Exception;
	
	public void removeArticle(int articleNO) throws Exception;
	
	
	
	
	public List<BoardAttachVO> getAttachList(int articleNO);
	
	public boolean remove(int articleNO) throws Exception;
	
	public boolean modify(ArticleVO articleVO, Map articleMap) throws Exception;
	*/
}
