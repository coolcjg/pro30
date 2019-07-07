package com.myspring.pro30.board.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;

import lombok.extern.log4j.Log4j;

@Log4j
@Service("boardService")
@Transactional(propagation=Propagation.REQUIRED)
public class BoardServiceImpl implements BoardService{
	@Autowired
	BoardDAO boardDAO;
	
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
	public List<BoardAttachVO> getAttachList(int articleNO){
		
		log.info("get Attach list by articleNO : " + articleNO);
		
		return boardDAO.getAttachList2(articleNO);
	}
	
	@Override
	public void addNewArticleAttach(Map articleMap, ArticleVO articleVO) throws Exception{
		boardDAO.addNewArticleAttach2(articleMap, articleVO);
	}
	
	
	
	@Override
	public List<ArticleVO> listArticles() throws Exception{
		List<ArticleVO> articlesList = boardDAO.selectAllArticlesList();
		return articlesList;
	}
	
	@Override
	public List<ArticleVO> listArticlesWithPaging(Criteria cri) throws Exception{
		List<ArticleVO> articlesList = boardDAO.selectArticlesWithPaging(cri);
		return articlesList;
	}
	
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		return boardDAO.insertNewArticle(articleMap);

	}
	
	@Override 
	public void modArticle(Map articleMap) throws Exception{
		boardDAO.updateArticle(articleMap);
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception{
		boardDAO.deleteArticle(articleNO);
	}
	
	@Override
	public ArticleVO viewArticle(int articleNO) throws Exception{
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		return articleVO;		
	}
	
	@Override
	public int serviceGetTotal(Criteria cri) {
		return boardDAO.getTotal(cri);
	}
	

	

	


}
