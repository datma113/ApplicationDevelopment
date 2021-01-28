package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import entity.Khachhang;
import session_factory.MySessionFactory;

/**
 * 05/12/2020
 * 
 * @author kienc
 *
 */
public class DaoKhachhang {

	private SessionFactory sessionFactory;
	private FullTextSession fullTextSession;
	private QueryBuilder qb;

	public DaoKhachhang() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		fullTextSession = MySessionFactory.getInstance().getFullTextSession();
		qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Khachhang.class).get();
	}

	/**
	 * 05/12/2020 lấy khách hàng theo mã khách hàng
	 * 
	 * @param id
	 * @return
	 */
	public Khachhang getByID(String id) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			Khachhang kh = session.find(Khachhang.class, id);
			tran.commit();
			return kh;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 05/12/2020 lấy ra danh sách khách hàng theo trang, offset là vị trí bắt đầu,
	 * row là số dòng muốn lấy ra
	 * 
	 * @param offset
	 * @param row
	 * @return
	 */
	public List<Khachhang> getDsKh(int offset, int row) {
		String s = "Select * from Khachhang ORDER BY makhachhang OFFSET :x ROWS FETCH NEXT :y ROWS ONLY";
		List<Khachhang> list = new ArrayList<Khachhang>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createNativeQuery(s, Khachhang.class).setParameter("x", offset).setParameter("y", row)
					.getResultList();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * 05/12/2020 tìm kiếm khách hàng theo mã họ tên hoặc số điện thoại, dùng
	 * textsearch
	 * 
	 * @param offset
	 * @param row
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Khachhang> timkiem(int offset, int row, String keyword) {
		List<Khachhang> list = new ArrayList<Khachhang>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("makhachhang", "hoten", "sodienthoai").matching(keyword).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Khachhang.class);
		Transaction tran = fullTextSession.getTransaction();
		try {
			tran.begin();
			list = hibQuery.setMaxResults(row).setFirstResult(offset).getResultList();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		}
		return list;
	}

	/**
	 * 05/12/2020 lấy ra size số khách hàng tìm kiếm được
	 * 
	 * @param offset
	 * @param row
	 * @param keyword
	 * @return
	 */
	public int getSizeTimkiem(String keyword) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("makhachhang", "hoten", "sodienthoai").matching(keyword).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Khachhang.class);
		Transaction tran = fullTextSession.getTransaction();
		try {
			tran.begin();
			size = hibQuery.getResultSize();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		}
		return size;
	}

	/**
	 * 05/12/2020 lấy ra tổng số KH trong CSDL
	 * 
	 * @return
	 */
	public int getSize() {
		Session session = sessionFactory.openSession();
		String s = "SELECT COUNT(*) as tongsoluong FROM Khachhang";
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<?> result = session.createNativeQuery(s).getResultList();
			tran.commit();
			try {
				return Integer.parseInt(result.get(0).toString());
			} catch (Exception e) {
			}
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return 0;
	}

	/**
	 * 05/12/2020 thêm KH vào CSDL
	 * 
	 * @param kh
	 * @return
	 */
	public boolean themKhachHang(Khachhang kh) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.save(kh);
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * 05/12/2020 cập nhật thông tin 1 khách hàng
	 * 
	 * @param khNew
	 * @return
	 */
	public boolean capNhatKhachHang(Khachhang khNew) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.update(khNew);
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * 05/12/2020 xóa KH
	 * 
	 * @param maKH
	 * @return
	 */
	/*
	 * public boolean xoaKhachHang(Khachhang khachHang) { Session session =
	 * sessionFactory.openSession(); Transaction tran = session.getTransaction();
	 * try { tran.begin(); session.delete(khachHang); tran.commit(); return true; }
	 * catch (Exception e) { tran.rollback(); } finally { session.close(); } return
	 * false; }
	 */

	/**
	 * 05/12/2020 lấy ra mã KH cuối cùng trong bảng
	 * 
	 * @return
	 */
	public String getMaxMaKH() {
		String s = "SELECT max(makhachhang) as maxmakhachhang from Khachhang";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<?> result = session.createNativeQuery(s).getResultList();
			tran.commit();
			try {
				return result.get(0).toString();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 05/12/2020 lấy ra mã KH tự động tăng tiếp theo
	 * 
	 * @return
	 */
	public String getNextMaKH() {
		String maxMaKH = getMaxMaKH();
		if (maxMaKH == null) {
			maxMaKH = "00000000";
		} else {
			maxMaKH = maxMaKH.substring(2, maxMaKH.length());
		}
		int identity = 0;
		try {
			identity = Integer.parseInt(maxMaKH);
		} catch (Exception e) {
		}
		String StrIdentity = identity + 1 + "";
		String s = "KH00000000";
		String newMaKH = s.substring(0, s.length() - StrIdentity.length()) + StrIdentity;
		return newMaKH;
	}

}