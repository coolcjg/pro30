package com.myspring.pro30.board.service;

import java.util.List;
import java.util.Map;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;

public interface BoardService {
	public List<ArticleVO> listArticles() throws Exception;
	public List<ArticleVO> listArticlesWithPaging(Criteria cri) throws Exception;
	
	public int addNewArticle(Map articleMap) throws Exception;
	public void addNewArticleAttach(Map articleMap, ArticleVO articleVO) throws Exception;
	
	public ArticleVO viewArticle(int articleNO) throws Exception;
	
	public void modArticle(Map articleMap) throws Exception;
	
	public void removeArticle(int articleNO) throws Exception;
	
	public int serviceGetTotal(Criteria cri);
	
	
	public List<BoardAttachVO> getAttachList(int articleNO);
	
	public boolean remove(int articleNO) throws Exception;
	
	public boolean modify(ArticleVO articleVO, Map articleMap) throws Exception;
}
