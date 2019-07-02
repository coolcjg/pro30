package com.myspring.pro30.gallery.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.gallery.vo.GalleryVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository("GalleryDAO") 
public class GalleryDAOImpl implements GalleryDAO{
	
	@Autowired
	private SqlSession sqlSession;
	

	@Override
	public List list(Criteria cri) throws Exception{
		List<GalleryVO> articlesList = sqlSession.selectList("mapper.gallery.list",cri);
		return articlesList;
	}
	
	@Override
	public int getTotal(Criteria cri) throws DataAccessException{
		return sqlSession.selectOne("mapper.gallery.getTotalCount", cri);
	}
	
	@Transactional
	@Override
	public void add(Map articleMap, GalleryVO galleryVO) throws DataAccessException{
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		
		sqlSession.insert("mapper.gallery.insertNewArticle", articleMap);

		if(galleryVO.getAttachList()==null || galleryVO.getAttachList().size()<=0) {
			return;
		}
		
		galleryVO.getAttachList().forEach(attach->{
			attach.setArticleNO(articleNO);
			sqlSession.insert("mapper.attach.insertGallery", attach);			
		});
	}
	
	private int selectNewArticleNO() throws DataAccessException{
		return sqlSession.selectOne("mapper.gallery.selectNewArticleNO");
	}
	
	
	public BoardAttachVO thumbnail(int articleNO) throws DataAccessException{
		return sqlSession.selectOne("mapper.attach.thumbnail", articleNO);
	}
	
	
	
	/*
	
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
	public boolean updateArticle(Map articleMap) throws DataAccessException{
		return sqlSession.update("mapper.board.updateArticle", articleMap)==1;
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
	
	
	
	private int selectNewImageFileNO() throws DataAccessException{
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	}
	
	*/
	
}