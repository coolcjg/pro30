package com.myspring.pro30.common.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.vo.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;






  
@Controller
@Log4j
public class UploadController {
	
	@RequestMapping(value="/board/uploadForm2.do", method= {RequestMethod.GET})
	public ModelAndView uploadForm(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String)request.getAttribute("viewName");
		log.info("upload form");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@PostMapping("/board/uploadFormAction.do")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model ) {
		
		String uploadFolder = "C:\\upload";
		
		for(MultipartFile multipartFile:uploadFile) {
			log.info("------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile);
			}catch(Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	
	@GetMapping("/board/uploadAjax.do")
	public ModelAndView uploadAjax(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		log.info("upload ajax");
		return mav;
	}
	
	@PostMapping(value = "/board/uploadAjaxAction.do", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile, HttpServletRequest request, HttpServletResponse response) {

		log.info("update ajax post...........");
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";
		
		String uploadFolderPath=getFolder();
		
		//make folder
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path: " + uploadPath);
		
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
				
		for(MultipartFile multipartFile: uploadFile) {
			AttachFileDTO attachDTO = new AttachFileDTO();
			log.info("---------------------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			log.info("only file name : " + uploadFileName);
			attachDTO.setFileName(uploadFileName);
			
			//파일중복을 위한 uuid
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				//이미지 파일인지 체크 후 섬네일 생성
				if(checkImageType(saveFile)) {
					
					attachDTO.setImage(true);
					
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail,100,100);
					thumbnail.close();
				}
				
				list.add(attachDTO);
				
				
			}catch(Exception e) {
				log.error(e.getMessage());
			}
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	
	@GetMapping("/board/display.do")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName: " + fileName);
		File file = new File("c:\\upload\\"+fileName);
		
		log.info("file: " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*파일 다운로드를 위해 OCTET_STREAM_VALUE로 설정.
	 * 다운로드시 저장되는 이름은 Content-Disposition으로 지정.
	 * 
	 */
	@GetMapping(value="/board/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent")String userAgent,String fileName){
		log.info("download file : " + fileName);
		Resource resource = new FileSystemResource("c:\\upload\\"+fileName);
		log.info("resource : " + resource);
		
		if(resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String resourceName = resource.getFilename();
		
		//remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
				
		HttpHeaders headers = new HttpHeaders();
		try {
			String downloadName = null;
			
			if(userAgent.contains("Trident")) {
				log.info("IE browser");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+"," ");
			}else if(userAgent.contains("Edge")) {
				log.info("Edge browser");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				log.info("Edge name : " + downloadName);
			}else {
				log.info("Chrome browser");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			log.info("downloadName : " + downloadName);
				
			
			headers.add("Content-Disposition", "attachment; filename="+downloadName);
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);

	}
	
	
	
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
