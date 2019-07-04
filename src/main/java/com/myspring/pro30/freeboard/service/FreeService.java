package com.myspring.pro30.freeboard.service;

import java.util.List;
import java.util.Map;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.gallery.vo.GalleryVO;

public interface FreeService {

	public List<ArticleVO> list(Criteria cri) throws Exception;
	public int serviceGetTotal(Criteria cri);
	public void add(Map articleMap, GalleryVO galleryVO) throws Exception;
	public GalleryVO view(int articleNO) throws Exception;
	public List<BoardAttachVO> getAttachList(int articleNO);
	public boolean modify(GalleryVO galleryVO, Map articleMap) throws Exception;
	public boolean remove(int articleNO) throws Exception;	
	/*
	
	
	public BoardAttachVO thumbnail(int articleNO) throws Exception;
	

	
	
	
	

	*/
}
