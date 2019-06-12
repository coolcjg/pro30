package com.myspring.pro30.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.ImageVO;


@Service("boardService")
@Transactional(propagation=Propagation.REQUIRED)
public class BoardServiceImpl implements BoardService{
	@Autowired
	BoardDAO boardDAO;
	
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
		int articleNO = boardDAO.insertNewArticle(articleMap);
		//articleMap.put("articleNO", articleNO);
		boardDAO.insertNewImage(articleMap);
		return articleNO;
	}
	
	@Override 
	public void modArticle(Map articleMap) throws Exception{
		boardDAO.updateArticle(articleMap);
		int articleNO = Integer.parseInt((String)articleMap.get("articleNO"));
		System.out.println("BoardServiceImpl에서 articleNO : "+articleNO);
		articleMap.put("articleNO", articleNO);
		boardDAO.insertNewImage(articleMap);
		System.out.println("BoardServiceImpl 종료");
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception{
		boardDAO.deleteArticle(articleNO);
	}
	
	@Override
	public Map viewArticle(int articleNO) throws Exception{
		Map articleMap = new HashMap();
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
		articleMap.put("article", articleVO);
		articleMap.put("imageFileList", imageFileList);
		return articleMap;	
		
	}
	

	


}
