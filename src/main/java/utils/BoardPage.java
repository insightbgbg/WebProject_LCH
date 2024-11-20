package utils;

public class BoardPage {

/*	public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum, String url) {
	    StringBuilder pagingStr = new StringBuilder();

	    // 실제 페이징 계산 로직
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    // 페이지가 1개인 경우 페이징 링크를 표시하지 않음
	    if (totalPage <= 1) {
	        return ""; // 빈 문자열 반환
	    }

	    
	    int startPage = ((pageNum - 1) / blockPage) * blockPage + 1;
	    int endPage = startPage + blockPage - 1;
	    if (endPage > totalPage) endPage = totalPage;

	    // 이전 페이지
	    if (pageNum > blockPage) {
	        pagingStr.append("<a href='").append(url).append("&pageNum=").append(startPage - 1).append("'>Previous</a>");
	    }

	    // 페이지 번호
	    for (int i = startPage; i <= endPage; i++) {
	        if (i == pageNum) {
	            pagingStr.append("<span>").append(i).append("</span>");
	        } else {
	            pagingStr.append("<a href='").append(url).append("&pageNum=").append(i).append("'>").append(i).append("</a>");
	        }
	    }

	    // 다음 페이지
	    if (endPage < totalPage) {
	        pagingStr.append("<a href='").append(url).append("&pageNum=").append(endPage + 1).append("'>Next</a>");
	    }

	    return pagingStr.toString();
	}

} */
	
	public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum, String baseUrl) {
	    StringBuilder pagingStr = new StringBuilder();

	    int totalPages = (int) Math.ceil((double) totalCount / pageSize); // 총 페이지 수
	    int currentBlock = (int) Math.ceil((double) pageNum / blockPage); // 현재 블록
	    int startPage = (currentBlock - 1) * blockPage + 1; // 블록 시작 페이지
	    int endPage = Math.min(currentBlock * blockPage, totalPages); // 블록 끝 페이지

	    pagingStr.append("<nav aria-label='Page navigation'>");
	    pagingStr.append("<ul class='pagination justify-content-center'>");

	    // 이전 블록으로 이동
	    if (currentBlock > 1) {
	        pagingStr.append("<li class='page-item'>");
	        pagingStr.append("<a class='page-link' href='").append(baseUrl).append("&pageNum=").append(startPage - 1)
	                 .append("' aria-label='Previous'>");
	        pagingStr.append("<span aria-hidden='true'>&laquo;</span>");
	        pagingStr.append("</a></li>");
	    } else {
	        pagingStr.append("<li class='page-item disabled'>");
	        pagingStr.append("<span class='page-link'>&laquo;</span>");
	        pagingStr.append("</li>");
	    }

	    // 페이지 번호 출력
	    for (int i = startPage; i <= endPage; i++) {
	        if (i == pageNum) {
	            pagingStr.append("<li class='page-item active'><span class='page-link'>").append(i).append("</span></li>");
	        } else {
	            pagingStr.append("<li class='page-item'>");
	            pagingStr.append("<a class='page-link' href='").append(baseUrl).append("&pageNum=").append(i)
	                     .append("'>").append(i).append("</a>");
	            pagingStr.append("</li>");
	        }
	    }

	    // 다음 블록으로 이동
	    if (currentBlock < (int) Math.ceil((double) totalPages / blockPage)) {
	        pagingStr.append("<li class='page-item'>");
	        pagingStr.append("<a class='page-link' href='").append(baseUrl).append("&pageNum=").append(endPage + 1)
	                 .append("' aria-label='Next'>");
	        pagingStr.append("<span aria-hidden='true'>&raquo;</span>");
	        pagingStr.append("</a></li>");
	    } else {
	        pagingStr.append("<li class='page-item disabled'>");
	        pagingStr.append("<span class='page-link'>&raquo;</span>");
	        pagingStr.append("</li>");
	    }

	    pagingStr.append("</ul>");
	    pagingStr.append("</nav>");

	    return pagingStr.toString();
	}

	
}	
