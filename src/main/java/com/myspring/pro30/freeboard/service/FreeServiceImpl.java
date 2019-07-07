package com.myspring.pro30.freeboard.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.freeboard.dao.FreeDAO;
import com.myspring.pro30.gallery.vo.GalleryVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Service("FreeService")
@Transactional(propagation=Propagation.REQUIRED)
public class FreeServiceImpl implements FreeService {
	@Autowired
	FreeDAO freeDAO;
	
	
	@Override
	public List<ArticleVO> list(Criteria cri) throws Exception{
		List<ArticleVO> articlesList = freeDAO.list(cri);
		return articlesList;
	}
	
	@Override
	public int serviceGetTotal(Criteria cri) {
		return freeDAO.getTotal(cri);
	}
	
	@Override
	public void add(Map articleMap, GalleryVO galleryVO) throws Exception{
		freeDAO.add(articleMap, galleryVO);
	}
	
	@Override
	public GalleryVO view(int articleNO) throws Exception{
		GalleryVO galleryVO = freeDAO.view(articleNO);
		return galleryVO;		
	}	
	
	@Override
	public List<BoardAttachVO> getAttachList(int articleNO){
		
		log.info("get Attach list by articleNO : " + articleNO);
		
		return freeDAO.getAttachList2(articleNO);
	}
	
	@Transactional
	@Override
	public boolean modify(GalleryVO galleryVO, Map articleMap) throws Exception {
		log.info("modify....... : " + galleryVO);
		
		freeDAO.deleteAll(galleryVO.getArticleNO());
		
		
		boolean modifyResult = freeDAO.updateArticle(articleMap);
		
		if(modifyResult && galleryVO.getAttachList() !=null && galleryVO.getAttachList().size()>0) {
			freeDAO.addAttach(articleMap, galleryVO);

		}
		
		
		return true;
	}	
	
	@Transactional
	@Override
	public boolean remove(int articleNO) throws Exception {
		log.info("remove...... : " + articleNO);
		
		freeDAO.deleteAll(articleNO);
		
		return freeDAO.deleteArticle(articleNO);
		
	}
}
