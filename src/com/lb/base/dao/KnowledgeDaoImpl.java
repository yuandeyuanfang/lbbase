package com.lb.base.dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lb.base.entity.Knowledge;
import com.lb.base.system.JdbcUtil;
import com.lb.base.util.Page;

public class KnowledgeDaoImpl implements KnowledgeDao{
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs =null;

	public List<Knowledge> query(Map map, Page page) {
		getConnection();
		List<Knowledge> list = new ArrayList<Knowledge>();
		String sql = "select * from (select rownum rn,t2.* from (select t.*,d.datatypename  from KNOWLEDGEPOINT t left join DATADICTIONARY d on t.pointtype = d.id order by t.id desc) t2) where rn > ? and rn < ? ";
		try {
			ps = con.prepareStatement(sql);
			ps.setLong(1, page.getBegin());//列索引从1开始，不是从0开始
			ps.setLong(2, page.getEnd());
			rs = ps.executeQuery();
			while(rs.next()){
				Knowledge knowledge = new Knowledge();
				knowledge.setId(rs.getLong(2));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setName(rs.getString(3));
				knowledge.setMenu(rs.getString("menu"));//通过数据库中表的列名取值
				knowledge.setPageLink(rs.getString(5));
				knowledge.setJavaPath(rs.getString(6));
				knowledge.setPointType(rs.getLong(7));
//				knowledge.setCreateTime(rs.getDate(8));//从结果集中取得日期部分2016-03-11	
//				knowledge.setCreateTime(rs.getTime(8));//从结果集中取得时间部分22：08：09
				knowledge.setCreateTime(new Date(rs.getTimestamp(8).getTime()));//从结果集中同时得到日期和时间2016-03-11 23：08：09
				knowledge.setPicture(rs.getBytes(9));//Blob类型字段
				knowledge.setArticle(rs.getString(10));//Clob类型字段
				knowledge.setPointTypeStr(rs.getString(11));
				list.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public int add(Knowledge knowledge) {
		getConnection();
		int num = 0;
		String sql = "insert into KNOWLEDGEPOINT(id,name,menu,pageLink,javaPath,pointType,picture,article) values(S_KNOWLEDGEPOINT_ID.NEXTVAL,?,?,?,?,?,?,?)";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, knowledge.getName());
			ps.setString(2, knowledge.getMenu());
			ps.setString(3, knowledge.getPageLink());
			ps.setString(4, knowledge.getJavaPath());
			ps.setLong(5, knowledge.getPointType());
//			ps.setDate(6, new java.sql.Date(knowledge.getCreateTime().getTime()));
			ps.setBytes(6, knowledge.getPicture());
			ps.setString(7, knowledge.getArticle());
//			File file = new File("C:\\Users\\Administrator\\Desktop\\许嵩\\小小\\b6a4322dd42a28349ab3b6415fb5c9ea14cebf65.jpg");
//			InputStream is = new BufferedInputStream(new FileInputStream(file));
//			ps.setBinaryStream(2, is,(int)file.length());
//			File fileC = new File("C:\\Users\\Administrator\\Desktop\\许嵩\\许嵩随笔.txt");
//			BufferedReader br = new BufferedReader(new FileReader(fileC));
//			ps.setCharacterStream(3, br, (int)fileC.length());
			num = ps.executeUpdate();
//			is.close();
//			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return num;
	}

	public int update(Knowledge knowledge) {
		getConnection();
		int num = 0;
		String sql = "update KNOWLEDGEPOINT set name = ? ,menu = ? ,pageLink = ? ,javaPath = ? ,pointType = ? ,picture = ? ,article=? where id = ?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, knowledge.getName());
			ps.setString(2, knowledge.getMenu());
			ps.setString(3, knowledge.getPageLink());
			ps.setString(4, knowledge.getJavaPath());
			ps.setLong(5, knowledge.getPointType());
			ps.setBytes(6, knowledge.getPicture());
			ps.setString(7, knowledge.getArticle());
			ps.setLong(8, knowledge.getId());
			num = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return num;
	}

	public int remove(Long id) {
		getConnection();
		int num = 0;
		String sql = "delete from KNOWLEDGEPOINT where id = ?";
		try {
			ps = con.prepareStatement(sql);
			ps.setLong(1, id);
			num = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return num;
	}

	public Knowledge findById(Long id) {
		String sql = "select t.*,d.datatypename from KNOWLEDGEPOINT t left join DATADICTIONARY d on t.pointtype = d.id  where t.id = ?";
		getConnection();
		Knowledge knowledge = new Knowledge();
		try {
			ps = con.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				knowledge.setId(rs.getLong(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setName(rs.getString(2));
				knowledge.setMenu(rs.getString("menu"));//通过数据库中表的列名取值
				knowledge.setPageLink(rs.getString(4));
				knowledge.setJavaPath(rs.getString(5));
				knowledge.setPointType(rs.getLong(6));
//				knowledge.setCreateTime(rs.getDate(7));//从结果集中取得日期部分2016-03-11	
//				knowledge.setCreateTime(rs.getTime(7));//从结果集中取得时间部分22：08：09
				knowledge.setCreateTime(new Date(rs.getTimestamp(7).getTime()));//从结果集中同时得到日期和时间2016-03-11 23：08：09
				knowledge.setPicture(rs.getBytes(8));//Blob类型字段
				knowledge.setArticle(rs.getString(9));//Clob类型字段
				knowledge.setPointTypeStr(rs.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return knowledge;
	}
	
	public int queryCount(Map map){
		getConnection();
		int count = 0;
		String sql = "select count(*) from (select t.*,d.datatypename  from KNOWLEDGEPOINT t left join DATADICTIONARY d on t.pointtype = d.id ) t2 ";
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	private void getConnection(){
		JdbcUtil jdbcUtil = new JdbcUtil();
		this.con = jdbcUtil.getConnection(con);
	}

}
