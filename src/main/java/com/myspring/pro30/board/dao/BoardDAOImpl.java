package com.myspring.pro30.board.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.ImageVO;

@Repository("boardDAO") 
public class BoardDAOImpl implements BoardDAO{
	
	@Autowired
	private SqlSession sqlSession;
	
	@Transactional
	@Override
	public void addNewArticleAttach2(Map articleMap, ArticleVO articleVO) throws DataAccessException{
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		
		if(articleVO.getAttachList()==null || articleVO.getAttachList().size()<=0) {
			return;
		}
		
		articleVO.getAttachList().forEach(attach->{
			attach.setArticleNo((long)articleNO);
			sqlSession.insert("mapper.attach.insert", attach);			
		});
		
		
	}
	
	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException{
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		return articleNO;
	}
	
	@Override
	public List selectAllArticlesList() throws Exception{
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}
	
	@Override
	public List selectArticlesWithPaging(Criteria cri) throws Exception{
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectArticlesListWithPaging",cri);
		return articlesList;
	}
	

	
	
	private int selectNewArticleNO() throws DataAccessException{
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}
	
	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException{
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}
	
	@Override
	public void updateArticle(Map articleMap) throws DataAccessException{
		sqlSession.update("mapper.board.updateArticle", articleMap);
	}
	
	@Override
	public void deleteArticle(int articleNO) throws DataAccessException{
		sqlSession.delete("mapper.board.deleteArticle", articleNO);
	}
	
	@Override
	public void deleteOldFile(int articleNO) throws DataAccessException{
		sqlSession.delete("mapper.board.deleteOldImage", articleNO);
	}
	
	@Override
	public void insertNewImage(Map articleMap) throws DataAccessException{
		System.out.println("BoardDAOImpl 진입");
		List<ImageVO> imageFileList = (ArrayList)articleMap.get("imageFileList");
		System.out.println("BoardDAOImpl 진입2");
		int articleNO = (Integer)articleMap.get("articleNO");
		System.out.println("BoardDAOImpl 진입3");
		System.out.println(articleNO);
		int imageFileNO = selectNewImageFileNO();
		System.out.println(imageFileNO);
		for(ImageVO imageVO : imageFileList) {
			imageVO.setImageFileNO(++imageFileNO);
			imageVO.setArticleNO(articleNO);
		}
		sqlSession.insert("mapper.board.insertNewImage", imageFileList);
	}
	
	@Override
	public List selectImageFileList(int articleNO) throws DataAccessException{
		List<ImageVO> imageFileList = null;
		imageFileList = sqlSession.selectList("mapper.board.selectImageFileList", articleNO);
		return imageFileList;
	}
	
	@Override
	public int getTotal(Criteria cri) throws DataAccessException{
		return sqlSession.selectOne("mapper.board.getTotalCount", cri);
	}
	
	private int selectNewImageFileNO() throws DataAccessException{
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	}
	
}
