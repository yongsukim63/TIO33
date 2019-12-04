package BoardUi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import memvo.MEMBERVO;
import boardvo.BoardVO;



public class BoardDAO
{
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	

	public int insertMember(MEMBERVO board) {
		try {
			con=DBUtil.getCon();
			String sql= "insert into member values(member_seq.nextval,?,?,?,?)";
			ps=con.prepareStatement(sql);
			ps.setString(1, board.getId());
			ps.setString(2, board.getPassword());
			ps.setString(3, board.getName());
			ps.setInt(4, board.getGrade());

			int n = ps.executeUpdate();
			return n;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}finally {
			close();
		}
	}//--------------------------------------
	
	
	
	
	//로그인 메소드
	public ArrayList<MEMBERVO> Login(String Id){
		try {
			con=DBUtil.getCon();
			String sql = "SELECT member_no, id, password, name, grade FROM board WHERE id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, Id);
			rs = ps.executeQuery();

			ArrayList<MEMBERVO> arr = makeList(rs);

			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	
	//보드를 만드는 메소드
	public int AddBoard(BoardVO board){
		try {
			ArrayList<BoardVO> ab = null;
			con=DBUtil.getCon();
			String sql = "INSERT INTO BOARD VALUES(board_seq.nextval,?,?,?,SYSDATE)";
			ps = con.prepareStatement(sql);
			ps.setString(1, board.getTitle());
			ps.setString(2, board.getContent());
			ps.setString(3, board.getId());
			int n = ps.executeUpdate();
			return n;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			close();
		}
	}
	
	// 목록을 클릭하면 본문을 보여주는 메서드.

	public ArrayList<BoardVO> clickContent(int type)
	{
		try
		{
			con = DBConnection.getCon();
			
			String sql = "select boardnum, title, content from board where boardnum = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, type);
			rs = ps.executeQuery();
			
			ArrayList<BoardVO> arr = getList(rs);
			return arr;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			close();
		}
	}

	// 리스트 창에서 글 목록을 생성해 주는 메서드.
	public ArrayList<BoardVO> makeList()
	{
		try
		{
			con = DBConnection.getCon();
			String sql = "select boardnum, title, content, id, wdate from board";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList<BoardVO> arr = getList(rs);
			return arr;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			close();
		}
	}
	
	public ArrayList<MEMBERVO> makeList(ResultSet rs)
	throws SQLException{
		ArrayList<MEMBERVO> arr = new ArrayList<>();
		while(rs.next()) {
			int member_no = rs.getInt("member_no");
			String id = rs.getString("id");
			String password = rs.getString("password");
			String name = rs.getString("name");
			int grade = rs.getInt("grade");
			MEMBERVO voTemp = new MEMBERVO(member_no, id, password, name, grade);
			arr.add(voTemp);
		}//while-------------
		return arr;
	}
	
	// DB에서 ROW를 가져오는 메서드.
	public ArrayList<BoardVO> getList(ResultSet rs) throws SQLException
	{
		ArrayList<BoardVO> bvoArr = new ArrayList();
		while(rs.next())
		{
			BoardVO bvoTemp = new BoardVO(rs.getInt("boardnum"), rs.getString("title"), rs.getString("content"), rs.getString("id"), rs.getDate("wdate"));
			bvoArr.add(bvoTemp);
		}
		return bvoArr;
	}
	
	
	// 글 본문에서 글을 지우는 메서드.
	public int delete(Integer boardnum)
	{
		try
		{
			con = DBConnection.getCon();
			String sql = "delete from board where boardnum = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, boardnum);
			ps.execute();
			return boardnum;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		finally
		{
			close();
		}
	}
	
	// 글 본문에서 글을 수정하는 메서드.
		public int update (BoardVO vo)
		{
			try
			{
				con = DBConnection.getCon();
				String sql = "update board set content = '" + vo.getContent() + "' where boardnum = ?";
				ps = con.prepareStatement(sql);
				ps.setInt(1, vo.getBoardnum());
				ps.execute();
				return vo.getBoardnum();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				return -1;
			}
			finally
			{
				close();
			}
		}
	
	
	// DB로의 접속을 종료하는 메서드.
	private void close()
	{
		try
		{
			if(rs != null) rs.close();
			if(ps != null) ps.close();
			if(con != null) con.close();
		} 
		catch (Exception e)
		{

		}
	}
}
