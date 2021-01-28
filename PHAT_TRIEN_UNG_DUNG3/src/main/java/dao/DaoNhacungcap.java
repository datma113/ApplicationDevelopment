package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Nhacungcap;
import session_factory.MySessionFactory;

/**
 * 05/12/2020
 * 
 * @author kienc
 *
 */
public class DaoNhacungcap {

	private SessionFactory sessionFactory;

	public DaoNhacungcap() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
	}

	/**
	 * 05/12/2020 lấy ra nhà cung cấp khi biết mã nhà cung cấp
	 * 
	 * @param id
	 * @return
	 */
	public Nhacungcap getByID(String id) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			Nhacungcap ncc = session.find(Nhacungcap.class, id);
			tran.commit();
			return ncc;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 05/12/2020 lấy ra danh sách nhà cung cấp theo trang, offset là vị trí bắt
	 * đầu, row là số dòng
	 * 
	 * @param offset
	 * @param row
	 * @return
	 */
	public List<Nhacungcap> getDsNcc(int offset, int row) {
		String s = "Select * from Nhacungcap ORDER BY manhacungcap OFFSET :x ROWS FETCH NEXT :y ROWS ONLY";
		List<Nhacungcap> list = new ArrayList<Nhacungcap>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createNativeQuery(s, Nhacungcap.class).setParameter("x", offset).setParameter("y", row)
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
	 * 05/12/2020 lấy ra toàn bộ nhà cung cấp
	 * 
	 * @return
	 */
	public List<Nhacungcap> getAll() {
		List<Nhacungcap> list = new ArrayList<Nhacungcap>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createQuery("from Nhacungcap order by tennhacungcap", Nhacungcap.class).getResultList();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * 05/12/2020 đếm số nhà cung cấp hiện có trong bảng
	 * 
	 * @return
	 */
	public int getSize() {
		String s = "SELECT COUNT(*) as tongsoluong FROM Nhacungcap";
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
	 * 05/12/2020 thêm 1 nhà cung cấp mới
	 * 
	 * @param s
	 * @return
	 */
	public boolean themNhaCungCap(Nhacungcap ncc) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.save(ncc);
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
	 * 05/12/2020 xóa thông tin nhà cung cấp
	 * 
	 * @param mancc
	 * @return
	 */
	/*
	 * public boolean xoaNhaCungCap(Nhacungcap ncc) { Session session =
	 * sessionFactory.openSession(); Transaction tran = session.getTransaction();
	 * try { tran.begin(); session.delete(ncc); tran.commit(); return true; } catch
	 * (Exception e) { tran.rollback(); } finally { session.close(); } return false;
	 * }
	 */

	/**
	 * 05/12/2020 cập nhật thông tin nhà cung cấp
	 * 
	 * @param nccnew
	 * @return
	 */
	public boolean capNhatNhaCc(Nhacungcap nccnew) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.update(nccnew);
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
	 * 05/12/2020 lấy ra mã nhà cung cấp cuối cùng trong bảng
	 * 
	 * @return
	 */
	public String getMaxMaNCC() {
		String s = "SELECT max(manhacungcap) as maxmanhacungcap from Nhacungcap";
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
	 * 05/12/2020 lấy ra mã nhà cung cấp tự động tăng tiếp theo
	 * 
	 * @return
	 */
	public String getNextMaNCC() {
		String maxMaNCC = getMaxMaNCC();
		if (maxMaNCC == null) {
			maxMaNCC = "00000000";
		} else {
			maxMaNCC = maxMaNCC.substring(3, maxMaNCC.length());
		}
		int identity = 0;
		try {
			identity = Integer.parseInt(maxMaNCC);
		} catch (Exception e) {
		}
		String StrIdentity = identity + 1 + "";
		String s = "NCC00000000";
		String newMaNCC = s.substring(0, s.length() - StrIdentity.length()) + StrIdentity;
		return newMaNCC;
	}

}