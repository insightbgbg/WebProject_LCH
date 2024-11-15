package fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

// file upload
public class FileUtil {

	public static String uploadFile(HttpServletRequest req,
			String sDirectory)
			throws ServletException, IOException	{
			// 매개 변수 : request, directory
		
		/*
		 	file -> server 저장
		 	ofile : <input> tage name 
		 */
		Part part =req.getPart("ofile");
		 
		/*
		 	header -> filename
		 */
		String partHeader=part.getHeader("content-disposition");
		System.out.println("partHeader="+partHeader);
		
		/*
			header 값에서 "filename=" 이후의 문구 추출
			replace("\"", "") : 끝의 " 제거 (\ : escape letter)
		 */
		String[] phArr=partHeader.split("filename=");
		String originalFileName=phArr[1].trim()
				.replace("\"", "");
		
		/*
		 	part.write : file을 server에 저장
		 	File.separator : 경로 구분자 OS별 차등 고려 (윈도우 \, 리눅스 / 등)
		 */
		if (!originalFileName.isEmpty()) {
			part.write(sDirectory+File.separator + originalFileName);
		}

		// 저장된 originalFileName
		return originalFileName;
	}

	//파일명 변경 (한글명이 깨지는 문제 해결 목적)
	public static String renameFile(String sDirectory, String fileName) {
	
		// lastIndexOf(A) : A문자의 최종 위치
		// substring(A_index) : A_index 이후 문자열
		String ext=fileName.substring(fileName.lastIndexOf("."));
		
		// 
		/*
		  예: 2024년 11월 8일 오전 9시 15분 42초 357밀리초 -> 
 			20241108_91542357
		 */
		String now=new SimpleDateFormat("yyyyMMdd_HmsS")
				.format(new Date());
		
		String newFileName=now+ext;
		
		File oldFile = new File(sDirectory+File.separator+fileName);
		File newFile = new File(sDirectory+File.separator+newFileName);
		oldFile.renameTo(newFile);
		
		return newFileName;
		
	}

	// multiple files upload
	public static ArrayList<String> multipleFile(HttpServletRequest req,
			String sDirectory)
			throws ServletException, IOException	{

		// list collection
		ArrayList<String> listFileName=new ArrayList<>();
		
		// getParts : multiple files instances
		Collection<Part> parts =req.getParts();
		for(Part part : parts) {
			
			if(!part.getName().equals("ofile"))
				continue;
			
			String partHeader=part.getHeader("content-disposition");
			System.out.println("partHeader="+partHeader);
			
			/*
				header 값에서 "filename=" 이후의 문구 추출
				replace("\"", "") : 끝의 " 제거 (\ : escape letter)
			 */
			String[] phArr=partHeader.split("filename=");
			String originalFileName=phArr[1].trim()
					.replace("\"", "");
			
			/*
			 	part.write : file을 server에 저장
			 	File.separator : 경로 구분자 OS별 차등 고려 (윈도우 \, 리눅스 / 등)
			 */
			if (!originalFileName.isEmpty()) {
				part.write(sDirectory+File.separator + originalFileName);
			}
			listFileName.add(originalFileName);
		}			
		// 저장된 originalFileName
		return listFileName;
	}
		
	public static void download(HttpServletRequest req, HttpServletResponse resp,
            String directory, String sfileName, String ofileName) {
        String sDirectory = req.getServletContext().getRealPath(directory);
        try {
            // 파일을 찾아 입력 스트림 생성
            File file = new File(sDirectory, sfileName);
            InputStream iStream = new FileInputStream(file);

            // 한글 파일명 깨짐 방지
            String client = req.getHeader("User-Agent");
            if (client.indexOf("WOW64") == -1) {
                ofileName = new String(ofileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            else {
                ofileName = new String(ofileName.getBytes("KSC5601"), "ISO-8859-1");
            }

            // 파일 다운로드용 응답 헤더 설정
            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition",
                           "attachment; filename=\"" + ofileName + "\"");
            resp.setHeader("Content-Length", "" + file.length() );

            //out.clear();  // 출력 스트림 초기화

            // response 내장 객체로부터 새로운 출력 스트림 생성
            OutputStream oStream = resp.getOutputStream();

            // 출력 스트림에 파일 내용 출력
            byte b[] = new byte[(int)file.length()];
            int readBuffer = 0;
            while ( (readBuffer = iStream.read(b)) > 0 ) {
                oStream.write(b, 0, readBuffer);
            }

            // 입/출력 스트림 닫음
            iStream.close();
            oStream.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("예외가 발생하였습니다.");
            e.printStackTrace();
        }
    }

	public static void deleteFile(HttpServletRequest req, String directory, String filename) {
	    String sDirectory = req.getServletContext()
	                            .getRealPath(directory);
	    // (physical) path 
	    File file = new File(sDirectory + File.separator + filename);
	    // file이 있다면
	    if (file.exists()) {
	    	// 삭제
	    	file.delete();
	    }
	}
	
	
}

