package com.myspring.pro30.freeboard.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.gallery.dao.GalleryDAOImpl;
import com.myspring.pro30.gallery.vo.GalleryVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository("FreeDAO") 
public class FreeDAOImpl implements FreeDAO{


	@Autowired
	private SqlSession sqlSession;
	

	@Override
	public List list(Criteria cri) throws Exception{
		List<GalleryVO> articlesList = sqlSession.selectList("mapper.free.list",cri);
		return articlesList;
	}
	
	@Override
	public int getTotal(Criteria cri) throws DataAccessException{
		return sqlSession.selectOne("mapper.free.getTotalCount", cri);

	}	
	
	@Transactional
	@Override
	public void add(Map articleMap, GalleryVO galleryVO) throws DataAccessException{
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		
		sqlSession.insert("mapper.free.insertNewArticle", articleMap);

		if(galleryVO.getAttachList()==null || galleryVO.getAttachList().size()<=0) {
			return;
		}
		
		galleryVO.getAttachList().forEach(attach->{
			attach.setArticleNO(articleNO);
			sqlSession.insert("mapper.attach.insertFree", attach);			
		});
	}
	
	
	private int selectNewArticleNO() throws DataAccessException{

		return sqlSession.selectOne("mapper.free.selectNewArticleNO");

	}	
	
	@Override
	public GalleryVO view(int articleNO) throws DataAccessException{
		return sqlSession.selectOne("mapper.free.selectArticle", articleNO);
	}	
	
	@Override
	public List<BoardAttachVO> getAttachList2(int articleNO) throws DataAccessException{
		return sqlSession.selectList("mapper.attach.findByArticleNoFree", articleNO);
	}		
	
	@Override
	public void deleteAll(int articleNO) throws DataAccessException{
		sqlSession.delete("mapper.attach.deleteAllFree", articleNO);
	}
	
	
	@Override
	public boolean updateArticle(Map articleMap) throws DataAccessException{
		return sqlSession.update("mapper.free.updateArticle", articleMap)==1;
	}	
	
	@Override
	public void addAttach(Map articleMap,GalleryVO galleryVO) throws DataAccessException{
		
		log.info("첨부파일 배열 길이 : " + galleryVO.getAttachList().size());
		
		galleryVO.getAttachList().forEach(attach->{
			attach.setArticleNO(galleryVO.getArticleNO());
		
			log.info("attach articleNO : " + attach.getArticleNO());
			log.info("attach uuid : " + attach.getUuid());
			log.info("attach uploadPath : " + attach.getUploadPath());
			log.info("attach fileName : " + attach.getFileName());
			log.info("attach fileType : " + attach.isFileType());
			
			sqlSession.insert("mapper.attach.insertFree", attach);			
		});
	}
	
	@Override
	public boolean deleteArticle(int articleNO) throws DataAccessException{
		return sqlSession.delete("mapper.free.deleteArticle", articleNO)==1;
	}
}
