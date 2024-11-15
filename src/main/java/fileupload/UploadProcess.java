package fileupload;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// maping 항상 "/"로 시작 유의
@WebServlet("/13FileUpload/UploadProcess.do")
/*
	maxFileSize : 1file 당 1Mb
	maxRequestSize : 전체 file 10Mb
 */
@MultipartConfig(
		maxFileSize = 1024*1024*1,
		maxRequestSize =  1024*1024*10
)
public class UploadProcess extends HttpServlet {
	private static final long serialVersionUID=1L;
	
	// file upload - Post method
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			// physical save path
			String saveDirectory = getServletContext().getRealPath("/Uploads");
			// file upload
			String originalFileName = FileUtil.uploadFile(req,saveDirectory);
			// rename filename
			String saveFileName = FileUtil.renameFile(saveDirectory,originalFileName);
			// after upload, move to file list page
			resp.sendRedirect("FileList.jsp");
		}
		catch (Exception e) {
			e.printStackTrace();
			
			// upload 실패 시 request에 message 저장 후 forward
			req.setAttribute("errorMessage", "file upload error");
			req.getRequestDispatcher("FileUploadMain.jsp").forward(req, resp);
		}
	}

}
