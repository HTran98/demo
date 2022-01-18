package com.yellow.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yellow.model.Categories;



public class CategoryDAO {
	public static void main(String[] args) {
		//System.out.println(getUserId());
		System.out.println(getListCategory().get(0).getCategoryName());
	}
	
	public static List<Categories> getListCategory(){
		List<Categories> list = new ArrayList<Categories>();
		
		Categories category = null;
		String query = "selectAllCategory";
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			cstmt = conn.prepareCall(query);
			rs = cstmt.executeQuery();
			while(rs.next()) {
				category = new Categories();
				category.setCategoryId(rs.getString("categoryId"));
				category.setCategoryName(rs.getString("categoryName"));
				category.setCategoryDeleteStatus(rs.getInt("categoryDeleteStatus"));
				list.add(category);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (cstmt != null)
					cstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public static Categories getCategory(String categoryId) {
		Categories category = null;
		String query = "SELECT [categoryId] " + 
				"      ,[categoryName] " + 
				"      ,[categoryDeleteStatus] " + 
				"  FROM [dbo].[Categories] WHERE [categoryId] = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, categoryId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				category = new Categories();
				category.setCategoryId(rs.getString("categoryId"));
				category.setCategoryName(rs.getString("categoryName"));
				category.setCategoryDeleteStatus(rs.getInt("categoryDeleteStatus"));
				return category;
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
		return category;
	}
	
	public static String convertCategoryId(String categoryId) {
		int intCategoryId = Integer.parseInt(categoryId.substring(1,categoryId.length()));
		//xrSystem.out.println(intUserId);
		intCategoryId++;
		String strCategoryId = String.valueOf(intCategoryId);
		while(strCategoryId.length()<7) {
			strCategoryId = "0"+strCategoryId;
			//System.out.println(strUserId);
		}
		return "C"+strCategoryId;
	}
	public static String getCategoryId() {
		String query = "SELECT TOP 1 [categoryId] "+ 
				",[categoryName] " + 
				",[categoryDeleteStatus] " + 
				" FROM [dbo].[Categories] ORDER BY [categoryId] DESC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				
				return convertCategoryId(rs.getString("categoryId"));
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
		return "C0000001";
	}
	public static boolean InsertCategory(Categories category) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countInsert = 0;
		String query = "INSERT INTO [dbo].[Categories] " + 
				"           ([categoryId] " + 
				"           ,[categoryName] " + 
				"           ,[categoryDeleteStatus]) " + 
				"     VALUES " + 
				"           (? " + 
				"           ,? " + 
				"           ,?)";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, category.getCategoryId());
			pstmt.setString(2, category.getCategoryName());
			pstmt.setInt(3, category.getCategoryDeleteStatus());
			countInsert = pstmt.executeUpdate();
			if (countInsert == 1) {
				System.out.println("Insert category thanh cong.");
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

	public static boolean EditCategory(Categories category) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countUpdate = 0;
		String query = "UPDATE [dbo].[Categories] " + 
				"   SET [categoryName] = ? " + 
				"      ,[categoryDeleteStatus] = ? " + 
				" WHERE [categoryId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, category.getCategoryName());
			pstmt.setInt(2, category.getCategoryDeleteStatus());
			pstmt.setString(3, category.getCategoryId());
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
	public static boolean DeleteCategory(String categoryId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countDel = 0;
		String query = "UPDATE [dbo].[Categories] " + 
				"   SET [categoryDeleteStatus] = 1 " + 
				"       WHERE [categoryId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, categoryId);
			countDel = pstmt.executeUpdate();
			if (countDel == 1) {
				System.out.println("Delete category thanh cong.");
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
	public static List<Categories> searchCategory(String keyWords) {
		Connection conn = DBConnect.getConnect();
		List<Categories> lists = new ArrayList<Categories>();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call searchCategory(?) }";
		try {
			cstmt = conn.prepareCall(query);
			cstmt.setString(1, keyWords);
			rs = cstmt.executeQuery();
			while (rs.next())

			{
				Categories categories = new Categories();
				categories.setCategoryId(rs.getString(1));
				categories.setCategoryName(rs.getString(2));
				lists.add(categories);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return lists;
	}
}
