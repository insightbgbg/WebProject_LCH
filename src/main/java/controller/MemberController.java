package controller;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.WebProjectDAO;
import model.WebProjectDTO;
import utils.EmailSender;

@WebServlet("/member")
public class MemberController extends HttpServlet {
    private WebProjectDAO dao = new WebProjectDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
// github 연동 테스트2
// github 연동 test2        

        if ("update".equals(action)) {
            // 회원 정보 수정 처리
            HttpSession session = request.getSession(false);
            WebProjectDTO.Member loggedInUser = (session != null) ? (WebProjectDTO.Member) session.getAttribute("loggedInUser") : null;
            
            if (loggedInUser != null) {
                WebProjectDTO.Member updatedMember = new WebProjectDTO.Member();
                updatedMember.setMemberId(loggedInUser.getMemberId()); // ID는 변경 불가
                updatedMember.setPassword(request.getParameter("password"));
                updatedMember.setName(request.getParameter("name"));
                updatedMember.setEmail(request.getParameter("email"));
                updatedMember.setPhone(request.getParameter("phone"));
                updatedMember.setStatus(loggedInUser.getStatus());
                updatedMember.setRole(loggedInUser.getRole());
                
                dao.updateMember(updatedMember);
                
                // 세션 갱신
                session.setAttribute("loggedInUser", updatedMember);
                response.sendRedirect("update_success.jsp");
            } else {
                response.sendRedirect("login.jsp");
            }
        }         
        
        else if ("register".equals(action)) {
            // 회원 가입 처리
            String memberId = request.getParameter("memberId");

            // 중복 ID 확인
            if (dao.getMemberById(memberId) != null) {
                // 이미 존재하는 ID일 경우
                request.setAttribute("errorMessage", "The user ID is already taken. Please choose a different one.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            WebProjectDTO.Member member = new WebProjectDTO.Member();
            member.setMemberId(memberId);
            member.setPassword(request.getParameter("password"));
            member.setName(request.getParameter("name"));
            member.setEmail(request.getParameter("email"));
            member.setPhone(request.getParameter("phone"));

            dao.addMember(member);
            response.sendRedirect("register_success.jsp");        

        } else if ("forgotPassword".equals(action)) {
            String memberId = request.getParameter("memberId");
            String email = request.getParameter("email");

// 테스트 문구                            
            System.out.println("forgotPassword memberId : " + memberId);            
            System.out.println("forgotPassword email : " + email);
            
            WebProjectDTO.Member member = dao.getMemberByIdAndEmail(memberId, email);
            if (member != null) {
                // Generate a temporary password
                String tempPassword = UUID.randomUUID().toString().substring(0, 8);

// 테스트 문구                
                System.out.println("임시 password : " + tempPassword);                
                
                // Update the password in the database
                dao.updatePassword(memberId, tempPassword);

// 테스트 문구                
                System.out.println("password 업데이트 완료");
                
                // Send the temporary password via email
                String subject = "Temporary Password for Your Account";
                String message = "Hello, " + member.getName() + "\n\nYour temporary password is: " + tempPassword
                        + "\nPlease log in and change your password immediately.";
// 테스트 문구                
                System.out.println("메일 제목 : " + subject);
                System.out.println("메일 내용 : " + message);                

                
                EmailSender.send(email, subject, message);

                // Redirect to login page with success message
                request.setAttribute("successMessage", "A temporary password has been sent to your email.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                // Redirect back to forgot-password.jsp with error message
                request.setAttribute("errorMessage", "No account found with the provided ID and email.");
                request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            }
        
        } else if ("login".equals(action)) {
            // 로그인 처리
            String memberId = request.getParameter("memberId");
            String password = request.getParameter("password");

            WebProjectDTO.Member member = dao.getMemberById(memberId);

            if (member != null && member.getPassword().equals(password)) {
                // 로그인 성공
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", member);
                response.sendRedirect("home.jsp");
            } else {
                // 로그인 실패
                request.setAttribute("errorMessage", "Invalid ID or Password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");

        if (memberId != null) {
            // 특정 회원 정보 조회
            WebProjectDTO.Member member = dao.getMemberById(memberId);
            request.setAttribute("member", member);
            request.getRequestDispatcher("member_detail.jsp").forward(request, response);
        }
    }
}
