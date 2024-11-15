package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.WebProjectDAO;
import model.WebProjectDTO;

@WebServlet("/member")
public class MemberController extends HttpServlet {
    private WebProjectDAO dao = new WebProjectDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
// github 연동 테스트2
// github 연동 test2        
        if ("register".equals(action)) {
            // 회원 가입 처리
            WebProjectDTO.Member member = new WebProjectDTO.Member();
            member.setMemberId(request.getParameter("memberId"));
            member.setPassword(request.getParameter("password"));
            member.setName(request.getParameter("name"));
            member.setEmail(request.getParameter("email"));
            member.setPhone(request.getParameter("phone"));

            dao.addMember(member);
            response.sendRedirect("register_success.jsp");
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
