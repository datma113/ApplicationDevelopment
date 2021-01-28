package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Taikhoan;
import session_factory.MySessionFactory;

/**
 * 05/12/2020
 * 
 * @author kienc
 *
 */
public class DaoTaikhoan {

	private SessionFactory sessionFactory;

	public DaoTaikhoan() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
	}

	/**
	 * 05/12/2020 trả về tài khoản theo username và pasword truyền vào
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Taikhoan getTaikhoan(String username, String password) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		String query = "from Taikhoan where tendangnhap = :x and matkhau = :y";
		try {
			tran.begin();
			List<Taikhoan> result = session.createQuery(query, Taikhoan.class).setParameter("x", username)
					.setParameter("y", password).getResultList();
			tran.commit();
			try {
				return result.get(0);
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
	 * 05/12/2020 lấy ra danh sách tài khoản theo trang, offset là vị trí bắt đầu,
	 * row là số dòng
	 * 
	 * @param offset
	 * @param row
	 * @return
	 */
	public List<Taikhoan> getDsTk(int offset, int row) {
		String s = "Select * from Taikhoan ORDER BY tendangnhap OFFSET :x ROWS FETCH NEXT :y ROWS ONLY";
		List<Taikhoan> list = new ArrayList<Taikhoan>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createNativeQuery(s, Taikhoan.class).setParameter("x", offset).setParameter("y", row)
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
	 * 05/12/2020 đếm số tài khoản
	 * 
	 * @return
	 */
	public int getSize() {
		String sql = "SELECT COUNT(*) as tongsoluong FROM Taikhoan";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<?> result = session.createNativeQuery(sql).getResultList();
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
	 * 05/12/2020 cập nhật lại thông tin tài khoản
	 * 
	 * @param tknew
	 * @return
	 */
	public boolean capNhatTaiKhoan(Taikhoan tknew) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.update(tknew);
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
	 * 05/12/2020
	 * 
	 * @param taikhoan
	 * @return
	 */
	public boolean xoaTaiKhoan(Taikhoan taikhoan) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.delete(taikhoan);
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
	 * 05/12/2020 thêm 1 tài khoản đăng nhập mới
	 * 
	 * @param tknew
	 * @return
	 */
	public boolean themTaiKhoan(Taikhoan tknew) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.save(tknew);
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return false;
	}

}