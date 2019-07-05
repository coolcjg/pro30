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
import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.ImageVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository("boardDAO") 
public class BoardDAOImpl implements BoardDAO{
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<BoardAttachVO> getOldFiles() throws DataAccessException{
		return sqlSession.selectList("mapper.attach.getOldFiles");
	}
	
	@Override
	public void deleteAll(int articleNO) throws DataAccessException{
		sqlSession.delete("mapper.attach.deleteAll", articleNO);
	}
	
	@Override
	public boolean deleteArticle(int articleNO) throws DataAccessException{
		return sqlSession.delete("mapper.board.deleteArticle", articleNO)==1;
	}
	
	
	@Override
	public List<BoardAttachVO> getAttachList2(int articleNO) throws DataAccessException{
		return sqlSession.selectList("mapper.attach.findByArticleNo", articleNO);
	}
	
	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException{
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}
	
	
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
			attach.setArticleNO(articleNO);
			sqlSession.insert("mapper.attach.insert", attach);			
		});
	}
	
	@Override
	public void addAttach(Map articleMap,ArticleVO articleVO) throws DataAccessException{
		
		log.info("첨부파일 배열 길이 : " + articleVO.getAttachList().size());
		
		articleVO.getAttachList().forEach(attach->{
			attach.setArticleNO(articleVO.getArticleNO());
		
			log.info("attach articleNO : " + attach.getArticleNO());
			log.info("attach uuid : " + attach.getUuid());
			log.info("attach uploadPath : " + attach.getUploadPath());
			log.info("attach fileName : " + attach.getFileName());
			log.info("attach fileType : " + attach.isFileType());
			
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
	public boolean updateArticle(Map articleMap) throws DataAccessException{
		return sqlSession.update("mapper.board.updateArticle", articleMap)==1;
	}

	
	@Override
	public int getTotal(Criteria cri) throws DataAccessException{
		return sqlSession.selectOne("mapper.board.getTotalCount", cri);
	}
}
