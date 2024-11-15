package fileupload;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// maping 항상 "/"로 시작 유의
@WebServlet("/13FileUpload/MultipleProcess.do")
/*
	maxFileSize : 1file 당 1Mb
	maxRequestSize : 전체 file 10Mb
 */
@MultipartConfig(
		maxFileSize = 1024*1024*1,
		maxRequestSize =  1024*1024*10
)
public class MultipleProcess extends HttpServlet {
	private static final long serialVersionUID=1L;
	 
	// file upload - Post method
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			// physical save path
			String saveDirectory = getServletContext().getRealPath("/Uploads");
			// file upload
			ArrayList<String> listFileName= FileUtil.multipleFile(req, saveDirectory);
			for(String originalFileName : listFileName) {
				String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
			}
			
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
