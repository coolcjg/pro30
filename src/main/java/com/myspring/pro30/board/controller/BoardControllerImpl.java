package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.ImageVO;
import com.myspring.pro30.board.vo.PageDTO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController{
	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	
	@Autowired
	BoardService boardService;
	@Autowired
	ArticleVO articleVO;
	

	
	@Override
	@RequestMapping(value="/board/listArticles.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		return mav;
	}
	
	@Override
	@RequestMapping(value="/board/listArticlesWithPaging.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticlesWithPaging(HttpServletRequest request, HttpServletResponse response, Criteria cri) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		List articlesList = boardService.listArticlesWithPaging(cri);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		mav.addObject("pageMaker", new PageDTO(cri, 90));
		return mav;
	}
	
	

	  @Override
	  @RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
	  @ResponseBody
	  public ResponseEntity  addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String,Object> articleMap = new HashMap<String, Object>();
		
		Enumeration enu=multipartRequest.getParameterNames();
		
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			System.out.println(name + " : "+value);
			articleMap.put(name,value);
		}
		
		String imageFileName = upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		String id = memberVO.getId();
		articleMap.put("id",id);
		articleMap.put("imageFileName", imageFileName);
		

		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			int articleNO = boardService.addNewArticle(articleMap);
			if(imageFileName!=null && imageFileName.length()!=0) {
					
					File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
					//destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir,true);
				
			}
			    
			message = "<script>";
			message += " alert('새글 작성 완료');";
			message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticlesWithPaging.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		    
			 
		}catch(Exception e) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			 	srcFile.delete();

			

			message = " <script>";
			message +=" alert('새 글 등록 에러');');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	  }
	  
	/*
	  @RequestMapping(value="/board/modArticle.do", method=RequestMethod.POST)
		@ResponseBody
		public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, @ModelAttribute("Criteria") Criteria cri, HttpServletResponse response) throws Exception{
			multipartRequest.setCharacterEncoding("utf-8");
			String imageFileName = null;
			
			Map<String,Object> articleMap = new HashMap<String, Object>();
			Enumeration enu = multipartRequest.getParameterNames();
			
			//파라미터값을 map 객체에 저장.
			while(enu.hasMoreElements()) {
				String name = (String)enu.nextElement();
				String value = multipartRequest.getParameter(name);
				System.out.println("BoardControllerImpl에서 파라미터 찍기"+ name + " : "+value);
				articleMap.put(name, value);
			}
			
			//ID setting
			HttpSession session = multipartRequest.getSession();
			MemberVO memberVO = (MemberVO) session.getAttribute("member");
			String id = memberVO.getId();
			System.out.println("BoardControllerImpl에서 파라미터 찍기 id="+id);
			articleMap.put("id", id);
		
			 
			String imageFileName = upload(multipartRequest);
			List<ImageVO> imageFileList = new ArrayList<ImageVO>();
			if(fileList!=null && fileList.size()!=0) {
				for(String fileName:fileList) {
					ImageVO imageVO = new ImageVO();
					imageVO.setImageFileName(fileName);
					imageFileList.add(imageVO);
				}
				articleMap.put("imageFileList", imageFileList);
			}
			String articleNO = (String)articleMap.get("articleNO");
			System.out.println("BoardControllerImpl에서 파라미터 찍기 articleNO="+articleNO);
			
			String message;
			ResponseEntity resEnt = null;
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/html; charset=utf-8");
			try {
				boardService.modArticle(articleMap);
				if(imageFileList!=null && imageFileList.size()!=0) {
					for(ImageVO  imageVO:imageFileList) {
						imageFileName = imageVO.getImageFileName();
						File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
						File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
						//destDir.mkdirs();
						FileUtils.moveFileToDirectory(srcFile, destDir,true);
					}
				}

				int amount = cri.getAmount();
				int pageNum = cri.getPageNum();

				
				message = "<script>";
				message += " alert('수정완료');";
				
				//message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
				message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"?amount="+amount+"?pageNum="+pageNum+"';";
				message += "</script>";
				resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				
				
			}catch(Exception e) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				srcFile.delete();
				message = "<script>";
				message +=" alert('수정실패');";
				message +=" location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
				message +=" </script>";
				resEnt = new ResponseEntity(message, responseHeaders,HttpStatus.CREATED);
				
			}
			System.out.println("BoardControllerImpl modArticle함수 종료");
			return resEnt;
		}
	  */
	  

	@RequestMapping(value="/board/*Form.do", method=RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value="/board/viewArticle.do", method=RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, @ModelAttribute("cri") Criteria cri, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String) request.getAttribute("viewName");
		
		
		articleVO = boardService.viewArticle(articleNO);
		
		System.out.println("BoardController에서 가져온 게시글 정보------------" );
		System.out.println("ArticleNO = "+articleVO.getArticleNO() );
		System.out.println("title = "+articleVO.getTitle() );
		System.out.println("content = "+articleVO.getContent() );
		System.out.println("imageFileName = "+articleVO.getImageFileName() );
		System.out.println("id = "+articleVO.getId() );
		System.out.println("writeDate = "+articleVO.getWriteDate() );
		System.out.println("--------------------------------------------" );
		

		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", articleVO);
		return mav;
	}
	
	
	
	@Override
	@RequestMapping(value="/board/removeArticle.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html; charset=UTF-8");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
			FileUtils.deleteDirectory(destDir);
			
			message = "<script>";
			message +="alert('삭제 완료');";
			message +="location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +="</script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
		}catch(Exception e) {
			message = "<script>";
			message +="alert('삭제하지 못했습니다.');";
			message +="location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +="</script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
			
		}
		return resEnt;
	}
	
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		
		String imageFileName = null;
		Map<String, String> articleMap = new HashMap<String, String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName = mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO+"\\"+fileName);
			if(mFile.getSize()!=0) {
				if(! file.exists()) {
					if(file.getParentFile().mkdirs()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName));	
			}			
		}
		return imageFileName;
	}
}
