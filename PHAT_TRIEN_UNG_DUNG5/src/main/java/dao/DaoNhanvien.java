package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import entity.Nhanvien;
import session_factory.MySessionFactory;

/**
 * 05/12/2020
 * 
 * @author kienc
 *
 */
public class DaoNhanvien {

	private SessionFactory sessionFactory;
	private FullTextSession fullTextSession;
	private QueryBuilder qb;

	public DaoNhanvien() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		fullTextSession = MySessionFactory.getInstance().getFullTextSession();
		qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Nhanvien.class).get();
	}

	/**
	 * 05/12/2020 lấy ra tất cả thông tin của 1 nhân viên khi biết mã NV
	 * 
	 * @param manv
	 * @return
	 */
	public Nhanvien getNhanVien(String manv) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			Nhanvien nv = session.find(Nhanvien.class, manv);
			tran.commit();
			return nv;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * lấy ra toàn bộ nhân viên
	 * 
	 * @return
	 */
	public List<Nhanvien> getAll() {
		List<Nhanvien> list = new ArrayList<Nhanvien>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createQuery("from Nhanvien order by manhanvien", Nhanvien.class).getResultList();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * 05/12/2020 lấy ra danh sách nhân viên theo trang, offset là vị trí bắt đầu,
	 * row là số dòng
	 * 
	 * @param offset
	 * @param row
	 * @return
	 */
	public List<Nhanvien> getDsNv(int offset, int row) {
		String s = "Select * from Nhanvien ORDER BY manhanvien OFFSET :x ROWS FETCH NEXT :y ROWS ONLY";
		List<Nhanvien> list = new ArrayList<Nhanvien>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createNativeQuery(s, Nhanvien.class).setParameter("x", offset).setParameter("y", row)
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
	 * tìm kiếm nhân viên trên các trường đã đánh index
	 * 
	 * @param offset
	 * @param row
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Nhanvien> timkiem(int offset, int row, String keyword) {
		List<Nhanvien> list = new ArrayList<Nhanvien>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("manhanvien", "hodem", "ten", "socmnd", "sodienthoai").matching(keyword)
					.createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Nhanvien.class);
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
	 * lấy ra size kết quả tìm kiếm nhân viên ở hàm bên trên
	 * 
	 * @param keyword
	 * @return
	 */
	public int getSizeTimkiem(String keyword) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("manhanvien", "hodem", "ten", "socmnd", "sodienthoai").matching(keyword)
					.createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Nhanvien.class);
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
	 * 05/12/2020 đếm số lượng nhân viên
	 * 
	 * @return
	 */
	public int getSize() {
		String s = "SELECT COUNT(*) as tongsoluong FROM Nhanvien";
		Session session = sessionFactory.openSession();
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
	 * 05/12/2020 thêm 1 nhân viên mới
	 * 
	 * @param nv
	 * @return
	 */
	public boolean themNhanVien(Nhanvien nv) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.save(nv);
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
	 * 05/12/2020 cập nhật thông tin 1 nhân viên
	 * 
	 * @param nvNew
	 * @return
	 */
	public boolean capNhatNhanVien(Nhanvien nv) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.update(nv);
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
	 * 05/12/2020 xóa 1 nhân viên
	 * 
	 * @param maNV
	 * @return
	 */
	/*
	 * public boolean xoaNhanVien(Nhanvien nhanVien) { Session session =
	 * sessionFactory.openSession(); Transaction tran = session.getTransaction();
	 * try { tran.begin(); session.delete(nhanVien); tran.commit(); return true; }
	 * catch (Exception e) { tran.rollback(); } finally { session.close(); } return
	 * false; }
	 */

	/**
	 * 05/12/2020 lấy ra mã nhân viên cuối cùng trong bảng
	 * 
	 * @return
	 */
	public String getMaxMaNhanVien() {
		String s = "SELECT max(manhanvien) as maxmanhanvien from Nhanvien";
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
	 * 05/12/2020 lấy ra mã nhân viên tự động tăng tiếp theo
	 * 
	 * @return
	 */
	public String getNextMaNV() {
		String maxMaNV = getMaxMaNhanVien();
		if (maxMaNV == null) {
			maxMaNV = "00000000";
		} else {
			maxMaNV = maxMaNV.substring(2, maxMaNV.length());
		}
		int identity = 0;
		try {
			identity = Integer.parseInt(maxMaNV);
		} catch (Exception e) {
		}
		String StrIdentity = identity + 1 + "";
		String s = "NV00000000";
		String newMaNV = s.substring(0, s.length() - StrIdentity.length()) + StrIdentity;
		return newMaNV;
	}

}