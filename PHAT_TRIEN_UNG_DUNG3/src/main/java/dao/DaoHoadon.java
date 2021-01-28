package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import entity.Chitiethoadon;
import entity.Hoadon;
import entity.Khachhang;
import entity.Sanpham;
import session_factory.MySessionFactory;

/**
 * @author kienc
 *
 */
public class DaoHoadon {

	private SessionFactory sessionFactory;
	private FullTextSession fullTextSession;
	private QueryBuilder qb;

	public DaoHoadon() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		fullTextSession = MySessionFactory.getInstance().getFullTextSession();
		qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Hoadon.class).get();
	}

	/**
	 * lấy ra hóa đơn theo mã hóa đơn
	 * 
	 * @param mahd
	 * @return
	 */
	public Hoadon getHd(String mahd) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			Hoadon h = session.find(Hoadon.class, mahd);
			tran.commit();
			return h;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * lấy danh sách chi tiết hóa đơn theo mã hóa đơn
	 * 
	 * @param mahd
	 * @return
	 */
	public List<Chitiethoadon> getDsCthd(String mahd) {
		List<Chitiethoadon> list = new ArrayList<Chitiethoadon>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createQuery("from Chitiethoadon where mahoadon=: x", Chitiethoadon.class)
					.setParameter("x", mahd).getResultList();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * lấy danh sách hóa đơn theo trang, bắt đầu từ vị trí offset, row là số dòng
	 * muốn lấy ra
	 * 
	 * @param offset
	 * @param row
	 * @return
	 */
	public List<Hoadon> getDsHd(int offset, int row) {
		String query = "Select * from Hoadon ORDER BY mahoadon OFFSET :x ROWS FETCH NEXT :y ROWS ONLY";
		List<Hoadon> list = new ArrayList<Hoadon>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createNativeQuery(query, Hoadon.class).setParameter("x", offset).setParameter("y", row)
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
	 * tìm kiếm hóa đơn trên index đã nhúng
	 * 
	 * @param offset
	 * @param row
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Hoadon> timkiem(int offset, int row, String keyword) {
		List<Hoadon> list = new ArrayList<Hoadon>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword()
					.onFields("mahoadon", "khachhang.makhachhang", "khachhang.hoten", "khachhang.sodienthoai",
							"nhanvien.manhanvien", "nhanvien.hodem", "nhanvien.ten", "nhanvien.sodienthoai",
							"nhanvien.socmnd")
					.matching(keyword).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Hoadon.class);
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
	 * lấy size kết quả tìm kiếm hóa đơn ở hàm timkiem
	 * 
	 * @param keyword
	 * @return
	 */
	public int getSizeTimkiem(String keyword) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword()
					.onFields("mahoadon", "khachhang.makhachhang", "khachhang.hoten", "khachhang.sodienthoai",
							"nhanvien.manhanvien", "nhanvien.hodem", "nhanvien.ten", "nhanvien.sodienthoai",
							"nhanvien.socmnd")
					.matching(keyword).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Hoadon.class);
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
	 * đếm tổng số lượng các hóa đơn trong bảng
	 * 
	 * @return
	 */
	public int getSize() {
		String s = "SELECT COUNT(*) as tongsoluong FROM Hoadon";
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
	 * thêm 1 hóa đơn vào CSDL
	 * 
	 * @param h: hóa đơn
	 * @return
	 */
	public boolean themHD(Hoadon h) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			/*
			 * nếu là khách hàng mới thì thêm khách hàng vào cơ sở dữ liệu
			 */
			if (session.find(Khachhang.class, h.getKhachhang().getMakhachhang()) == null)
				session.save(h.getKhachhang());
			session.save(h);
			h.getChitiethoadons().forEach(x -> {
				x.setHoadon(h);
				session.save(x);
				Sanpham sp = x.getSanpham();
				sp.setSoluongton(sp.getSoluongton() - x.getSoluong());
				session.update(sp);
			});
			tran.commit();
			/*
			 * cập nhật lại index cho các sản phẩm vừa mua
			 */
			h.getChitiethoadons().forEach(x -> {
				Transaction tx = fullTextSession.getTransaction();
				try {
					tx.begin();
					fullTextSession.index(x.getSanpham());
					tx.commit();
				} catch (Exception e) {
					tx.rollback();
				}
			});
			return true;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * lấy ra mã hóa đơn cuối cùng trong bảng
	 * 
	 * @return max của mã HĐ
	 */
	public String getMaxMaHD() {
		String s = "SELECT max(mahoadon) as maxmahoadon from Hoadon";
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
	 * lấy ra mã hóa đơn tự động tăng tiếp theo
	 * 
	 * @return mã hóa đơn tiếp theo để cấp tự động
	 */
	public String getNextMaHD() {
		String maxMaHD = getMaxMaHD();
		if (maxMaHD == null) {
			maxMaHD = "00000000";
		} else {
			maxMaHD = maxMaHD.substring(2, maxMaHD.length());
		}
		int identity = 0;
		try {
			identity = Integer.parseInt(maxMaHD);
		} catch (Exception e) {
		}
		String StrIdentity = identity + 1 + "";
		String s = "HD00000000";
		String newMaHD = s.substring(0, s.length() - StrIdentity.length()) + StrIdentity;
		return newMaHD;
	}

}
