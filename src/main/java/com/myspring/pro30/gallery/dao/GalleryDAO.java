package com.myspring.pro30.gallery.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.gallery.vo.GalleryVO;

public interface GalleryDAO {
	
	public List list(Criteria cri) throws Exception;
	
	public int getTotal(Criteria cri) throws DataAccessException;
	
	public void add(Map articleMap, GalleryVO galleryVO) throws DataAccessException;
	
	public BoardAttachVO thumbnail(int articleNO) throws DataAccessException;
	
	public GalleryVO view(int articleNO) throws DataAccessException;
	
	public List<BoardAttachVO> getAttachList2(int articleNO) throws DataAccessException;
	
	public void deleteAll(int articleNO) throws DataAccessException;
	
	public boolean updateArticle(Map articleMap) throws DataAccessException;
	
	public void addAttach(Map articleMap,GalleryVO galleryVO) throws DataAccessException;
	
	public boolean deleteArticle(int articleNO) throws DataAccessException;
	
	/*
	public int insertNewArticle(Map articleMap) throws DataAccessException;
	
	
	
	public void insertNewImage(Map articleMap) throws DataAccessException;
	public List selectImageFileList(int articleNO) throws DataAccessException;
	public void deleteOldFile(int articleNO) throws DataAccessException;
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<BoardAttachVO> getOldFiles() throws DataAccessException;
	*/
}
