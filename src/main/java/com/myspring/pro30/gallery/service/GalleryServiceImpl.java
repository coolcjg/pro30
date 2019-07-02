package com.myspring.pro30.gallery.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.gallery.dao.GalleryDAO;
import com.myspring.pro30.gallery.vo.GalleryVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Service("GalleryService")
@Transactional(propagation=Propagation.REQUIRED)
public class GalleryServiceImpl implements GalleryService{
	@Autowired
	GalleryDAO galleryDAO;
	
	
	@Override
	public List<ArticleVO> list(Criteria cri) throws Exception{
		List<ArticleVO> articlesList = galleryDAO.list(cri);
		return articlesList;
	}
	
	@Override
	public int serviceGetTotal(Criteria cri) {
		return galleryDAO.getTotal(cri);
	}
	
	@Override
	public void add(Map articleMap, GalleryVO galleryVO) throws Exception{
		galleryDAO.add(articleMap, galleryVO);
	}
	
	@Override
	public BoardAttachVO thumbnail(int articleNO) throws Exception{
		return galleryDAO.thumbnail(articleNO);
		
		
	}
	
	@Override
	public GalleryVO view(int articleNO) throws Exception{
		GalleryVO galleryVO = galleryDAO.view(articleNO);
		return galleryVO;		
	}	
	
	@Override
	public List<BoardAttachVO> getAttachList(int articleNO){
		
		log.info("get Attach list by articleNO : " + articleNO);
		
		return galleryDAO.getAttachList2(articleNO);
	}	
	
	
	/*
	
	@Transactional
	@Override
	public boolean remove(int articleNO) throws Exception {
		log.info("remove...... : " + articleNO);
		
		boardDAO.deleteAll(articleNO);
		
		return boardDAO.deleteArticle(articleNO);
		
	}
	
	@Transactional
	@Override
	public boolean modify(ArticleVO articleVO, Map articleMap) throws Exception {
		log.info("modify....... : " + articleVO);
		
		boardDAO.deleteAll(articleVO.getArticleNO());
		
		
		boolean modifyResult = boardDAO.updateArticle(articleMap);
		
		if(modifyResult && articleVO.getAttachList() !=null && articleVO.getAttachList().size()>0) {
			boardDAO.addAttach(articleMap, articleVO);

		}
		
		
		return true;
	}

	

	

	
	
	

	
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		return boardDAO.insertNewArticle(articleMap);

	}
	
	@Override 
	public void modArticle(Map articleMap) throws Exception{
		boardDAO.updateArticle(articleMap);
		int articleNO = Integer.parseInt((String)articleMap.get("articleNO"));
		System.out.println("BoardServiceImpl에서 articleNO : "+articleNO);
		articleMap.put("articleNO", articleNO);
		//boardDAO.insertNewImage(articleMap);
		System.out.println("BoardServiceImpl 종료");
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception{
		boardDAO.deleteArticle(articleNO);
	}
	

	

	*/

	


}
