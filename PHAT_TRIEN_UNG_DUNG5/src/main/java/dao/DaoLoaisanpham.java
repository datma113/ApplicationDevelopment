package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Loaisanpham;
import session_factory.MySessionFactory;

/**
 * 05/12/2020
 * 
 * @author kienc
 *
 */
public class DaoLoaisanpham {

	private SessionFactory sessionFactory;

	public DaoLoaisanpham() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
	}

	/**
	 * 05/12/2020 lấy ra loại sản phẩm theo mã loại
	 * 
	 * @param id
	 * @return
	 */
	public Loaisanpham getByID(String id) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			Loaisanpham loaisp = session.find(Loaisanpham.class, id);
			tran.commit();
			return loaisp;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 05/12/2020 lấy ra danh sách loại SP theo trang, offset là vi trí bắt đầu, row
	 * là số dòng
	 * 
	 * @param offset
	 * @param row
	 * @return
	 */
	public List<Loaisanpham> getDsLsp(int offset, int row) {
		String query = "Select * from Loaisanpham ORDER BY maloai OFFSET :x ROWS FETCH NEXT :y ROWS ONLY";
		List<Loaisanpham> list = new ArrayList<Loaisanpham>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createNativeQuery(query, Loaisanpham.class).setParameter("x", offset).setParameter("y", row)
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
	 * 05/12/2020 lấy ra toàn bộ danh sách loại sản phẩm
	 * 
	 * @return
	 */
	public List<Loaisanpham> getAll() {
		List<Loaisanpham> list = new ArrayList<Loaisanpham>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createQuery("from Loaisanpham order by tenloai", Loaisanpham.class).getResultList();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * 05/12/2020 đếm số loại sản phẩm
	 * 
	 * @return
	 */
	public int getSize() {
		Session session = sessionFactory.openSession();
		String s = "SELECT COUNT(*) as tongsoluong FROM Loaisanpham";
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
	 * 05/12/2020 thêm 1 loại sản phẩm mới
	 * 
	 * @param s
	 * @return
	 */
	public boolean themLoaiSanPham(Loaisanpham loaisp) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.save(loaisp);
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
	 * 05/12/2020 xóa 1 loại sản phẩm
	 * 
	 * @param maloai
	 * @return
	 */
	/*
	 * public boolean xoaLoaiSanPham(Loaisanpham loaiSp) { Session session =
	 * sessionFactory.openSession(); Transaction tran = session.getTransaction();
	 * try { tran.begin(); session.delete(loaiSp); tran.commit(); return true; }
	 * catch (Exception e) { tran.rollback(); } finally { session.close(); } return
	 * false; }
	 */

	/**
	 * 05/12/2020 cập nhật thông tin loại sản phẩm
	 * 
	 * @param lspnew
	 * @return
	 */
	public boolean capNhatLoaiSp(Loaisanpham lspnew) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.update(lspnew);
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
	 * 05/12/2020 lấy ra mã loại SP cuối cùng trong bảng
	 * 
	 * @return
	 */
	public String getMaxMaLoaiSp() {
		String s = "SELECT max(maloai) as maxmaloai from Loaisanpham";
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
	 * 05/12/2020 lấy ra mã loại SP tự động tăng tiếp theo
	 * 
	 * @return
	 */
	public String getNextMaLSP() {
		String maxMaLSP = getMaxMaLoaiSp();
		if (maxMaLSP == null) {
			maxMaLSP = "00000000";
		} else {
			maxMaLSP = maxMaLSP.substring(3, maxMaLSP.length());
		}
		int identity = 0;
		try {
			identity = Integer.parseInt(maxMaLSP);
		} catch (Exception e) {
		}
		String StrIdentity = identity + 1 + "";
		String s = "LSP00000000";
		String newMaLSP = s.substring(0, s.length() - StrIdentity.length()) + StrIdentity;
		return newMaLSP;
	}

}
