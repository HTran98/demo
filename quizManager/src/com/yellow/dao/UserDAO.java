package com.yellow.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yellow.model.Users;



public class UserDAO {
	public static void main(String[] args) {
		//System.out.println(getUserId());
	}
	public static Users getUser(String userId) {
		Users user = null;
		String query = " " + 
				"SELECT [userId] " + 
				"      ,[userName] " + 
				"      ,[passWord] " + 
				"      ,[fullName] " + 
				"      ,[userDeleteStatus] " + 
				"      ,[role] " + 
				"  FROM [dbo].[Users] WHERE [userid] = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new Users();
				user.setUserId(rs.getString("userId"));
				user.setUserName(rs.getString("userName"));
				user.setPassWord(rs.getString("passWord"));
				user.setFullName(rs.getString("fullName"));
				user.setUserDeleteStatus(rs.getInt("userDeleteStatus"));
				user.setRole(rs.getInt("role"));
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}
	public static Users getUserByName(String userName) {
		Users user = null;
		String query = " " + 
				"SELECT [userId] " + 
				"      ,[userName] " + 
				"      ,[passWord] " + 
				"      ,[fullName] " + 
				"      ,[userDeleteStatus] " + 
				"      ,[role] " + 
				"  FROM [dbo].[Users] WHERE [userName] = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new Users();
				user.setUserId(rs.getString("userId"));
				user.setUserName(rs.getString("userName"));
				user.setPassWord(rs.getString("passWord"));
				user.setFullName(rs.getString("fullName"));
				user.setUserDeleteStatus(rs.getInt("userDeleteStatus"));
				user.setRole(rs.getInt("role"));
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}
	public static String convertUser(String userId) {
		int intUserId = Integer.parseInt(userId.substring(1,userId.length()));
		//xrSystem.out.println(intUserId);
		intUserId++;
		String strUserId = String.valueOf(intUserId);
		while(strUserId.length()<7) {
			strUserId = "0"+strUserId;
			//System.out.println(strUserId);
		}
		return "U"+strUserId;
	}
	public static String getUserId() {
		String query = " " + 
				"SELECT TOP 1 [userId] " + 
				"      ,[userName] " + 
				"      ,[passWord] " + 
				"      ,[fullName] " + 
				"      ,[userDeleteStatus] " + 
				"      ,[role] " + 
				"  FROM [dbo].[Users] ORDER BY [userid] DESC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				
				return convertUser(rs.getString("userId"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "U0000001";
	}
	public static boolean InsertUser(Users user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countInsert = 0;
		String query = "INSERT INTO [dbo].[Users] " + 
				"           ([userId] " + 
				"           ,[userName] " + 
				"           ,[passWord] " + 
				"           ,[fullName] " + 
				"           ,[userDeleteStatus] " + 
				"           ,[role]) " + 
				"     VALUES " + 
				"           (? " + 
				"           ,? " + 
				"           ,? " + 
				"           ,? " + 
				"           ,? " + 
				"           ,?)";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getPassWord());
			pstmt.setString(4, user.getFullName());
			pstmt.setInt(5, user.getUserDeleteStatus());
			pstmt.setInt(6, user.getRole());
			countInsert = pstmt.executeUpdate();
			if (countInsert == 1) {
				System.out.println("Insert user thanh cong.");
				return true;
			}
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean EditUser(Users user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countUpdate = 0;
		String query = "UPDATE [dbo].[Users] " + 
				"   SET [userName] = ? " + 
				"      ,[passWord] = ? " + 
				"      ,[fullName] = ? " + 
				"      ,[userDeleteStatus] = ? " + 
				"      ,[role] = ? " + 
				" WHERE [userId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassWord());
			pstmt.setString(3, user.getFullName());
			pstmt.setInt(4, user.getUserDeleteStatus());
			pstmt.setInt(5, user.getRole());
			pstmt.setString(6, user.getUserId());
			countUpdate = pstmt.executeUpdate();
			if (countUpdate == 1) {
				System.out.println("Edit user thanh cong.");
				return true;
			}
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	public static boolean DeleteUser(String userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countDel = 0;
		String query = "UPDATE [dbo].[Users] " + 
				"   SET [userDeleteStatus] = 1 " + 
				"       WHERE [userId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			countDel = pstmt.executeUpdate();
			if (countDel == 1) {
				System.out.println("Delete user thanh cong.");
				return true;
			}
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	 public static List<Users> getAll() {
	        List<Users> lists = new ArrayList<Users>();
	        String query = " " + "SELECT [userId] " + "      ,[userName] " + "      ,[passWord] " + "      ,[fullName] "
	            + "      ,[userDeleteStatus] " + "      ,[role] " + "  FROM [dbo].[Users] WHERE [userDeleteStatus] = 0 ";
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try {
	            conn = DBConnect.getConnect();
	            pstmt = conn.prepareStatement(query);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                Users user = new Users();
	                user = new Users();
	                user.setUserId(rs.getString("userId"));
	                user.setUserName(rs.getString("userName"));
	                user.setPassWord(rs.getString("passWord"));
	                user.setFullName(rs.getString("fullName"));
	                user.setUserDeleteStatus(rs.getInt("userDeleteStatus"));
	                user.setRole(rs.getInt("role"));
	                lists.add(user);
	            }
	        }
	        catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        finally {
	            try {
	                if (rs != null)
	                    rs.close();
	                if (pstmt != null)
	                    pstmt.close();
	                if (conn != null)
	                    conn.close();
	            }
	            catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	        return lists;
	    }
	 public static List<Users> searchUser(String keyWords) {
			Connection conn = DBConnect.getConnect();
			List<Users> lists = new ArrayList<Users>();
			CallableStatement cstmt = null;
			ResultSet rs = null;
			String query = "{call searchUser(?) }";
			try {
				cstmt = conn.prepareCall(query);
				cstmt.setString(1, keyWords);
				rs = cstmt.executeQuery();
				while (rs.next())

				{
					Users users = new Users();
					users.setUserId(rs.getString(1));
					users.setUserName(rs.getString(2));
					users.setFullName(rs.getString(3));
					lists.add(users);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			return lists;
		}
}
