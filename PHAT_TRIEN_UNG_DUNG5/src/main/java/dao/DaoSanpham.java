package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import entity.Sanpham;
import session_factory.MySessionFactory;

/**
 * 05/12/2020
 * 
 * @author kienc
 *
 */
public class DaoSanpham {

	private SessionFactory sessionFactory;
	private FullTextSession fullTextSession;
	private QueryBuilder qb;

	public DaoSanpham() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		fullTextSession = MySessionFactory.getInstance().getFullTextSession();
		qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Sanpham.class).get();

	}

	/**
	 * 05/12/2020 lấy sản phẩm theo mã sản phẩm
	 * 
	 * @param id
	 * @return
	 */
	public Sanpham getById(String id) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			Sanpham s = session.find(Sanpham.class, id);
			tran.commit();
			return s;
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 05/12/2020 lấy danh sách sản phẩm theo trang, từ vị trí offset, lấy ra số
	 * dòng =row
	 * 
	 * @param offset
	 * @param row
	 * @return
	 */
	public List<Sanpham> getDsSp(int offset, int row) {
		String s = "Select * from Sanpham ORDER BY masanpham OFFSET :x ROWS FETCH NEXT :y ROWS ONLY";
		List<Sanpham> list = new ArrayList<Sanpham>();
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			list = session.createNativeQuery(s, Sanpham.class).setParameter("x", offset).setParameter("y", row)
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
	 * 05/12/2020 tìm kiếm text search trên các thuộc tính mã, tên, màu sắc, thương
	 * hiệu, mô tả
	 * 
	 * @param offset
	 * @param row
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sanpham> timkiem(int offset, int row, String keyword) {
		List<Sanpham> list = new ArrayList<Sanpham>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("masanpham", "tensanpham", "mota", "mausac", "thuonghieu").matching(keyword)
					.createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * 05/12/2020 lấy ra size danh sách sản phẩm tìm theo mã, tên, mô tả, màu sắc,
	 * thương hiệu
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
			query = qb.keyword().onFields("masanpham", "tensanpham", "mota", "mausac", "thuonghieu").matching(keyword)
					.createQuery();
		} catch (Exception e) {
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * 05/12/2020 tìm kiếm sản phẩm dựa vào tên của nhà cung cấp
	 * 
	 * @param offset
	 * @param row
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sanpham> timkiemTheoNhacc(int offset, int row, String keyword) {
		List<Sanpham> list = new ArrayList<Sanpham>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("nhacungcap.tennhacungcap").matching(keyword).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * 05/12/2020 lấy ra size danh sách sản phẩm tìm theo tên nhà cung cấp
	 * 
	 * @param keyword
	 * @return
	 */
	public int getSizeTimkiemTheoNhacc(String keyword) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("nhacungcap.tennhacungcap").matching(keyword).createQuery();
		} catch (Exception e) {
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * tìm kiêm sản phẩm dựa trên tên của loại sản phẩm
	 * 
	 * @param offset
	 * @param row
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sanpham> timkiemTheoLoaiSp(int offset, int row, String keyword) {
		List<Sanpham> list = new ArrayList<Sanpham>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("loaisanpham.tenloai").matching(keyword).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * 05/12/2020 lấy ra size danh sách sản phẩm tìm theo tên loại sản phẩm
	 * 
	 * @param keyword
	 * @return
	 */
	public int getSizeTimkiemTheoLoaiSp(String keyword) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.keyword().onFields("loaisanpham.tenloai").matching(keyword).createQuery();
		} catch (Exception e) {
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * tìm kiếm sản phẩm theo khoảng giá bán
	 * 
	 * @param offset
	 * @param row
	 * @param from
	 * @param to
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sanpham> timkiemTheoGiaban(int offset, int row, double from, double to) {
		List<Sanpham> list = new ArrayList<Sanpham>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.range().onField("giaban").from(from).to(to).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * lấy ra số lượng sản phẩm tìm được theo giá bán
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public int getSizeTimkiemTheoGiaban(double from, double to) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.range().onField("giaban").from(from).to(to).createQuery();
		} catch (Exception e) {
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * tìm kiếm theo khoảng thuế của sản phẩm
	 * 
	 * @param offset
	 * @param row
	 * @param from
	 * @param to
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sanpham> timkiemTheoThue(int offset, int row, double from, double to) {
		List<Sanpham> list = new ArrayList<Sanpham>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.range().onField("thue").from(from).to(to).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * lấy ra số lượng sản phẩm tìm được theo khoảng thuế
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public int getSizeTimkiemTheoThue(double from, double to) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.range().onField("thue").from(from).to(to).createQuery();
		} catch (Exception e) {
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * tìm kiếm theo số lượng tồn của sản phẩm
	 * 
	 * @param offset
	 * @param row
	 * @param from
	 * @param to
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sanpham> timkiemTheoSoluongton(int offset, int row, int from, int to) {
		List<Sanpham> list = new ArrayList<Sanpham>();
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.range().onField("soluongton").from(from).to(to).createQuery();
		} catch (Exception e) {
			/*
			 * không tìm thấy
			 */
			return list;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * lấy ra số lượng sản phẩm tìm được theo số lượng tồn
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public int getSizeTimkiemTheoSoluongton(int from, int to) {
		int size = 0;
		org.apache.lucene.search.Query query = null;
		try {
			query = qb.range().onField("soluongton").from(from).to(to).createQuery();
		} catch (Exception e) {
			return 0;
		}
		FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Sanpham.class);
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
	 * 05/12/2020 đếm số lượng sản phẩm trong bảng
	 * 
	 * @return
	 */
	public int getSize() {
		String s = "SELECT COUNT(*) as tongsoluong FROM Sanpham";
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
	 * 05/12/2020 thêm 1 sản phẩm mới
	 * 
	 * @param s
	 * @return
	 */
	public boolean themSanPham(Sanpham s) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.save(s);
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
	 * 05/12/2020 cập nhật thông tin 1 sản phẩm
	 * 
	 * @param spnew
	 * @return
	 */
	public boolean capNhatSanPham(Sanpham spnew) {
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			session.update(spnew);
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
	 * 05/12/2020 xoá sản phẩm
	 * 
	 * @param masp
	 * @return
	 */
	/*
	 * public boolean xoaSanPham(Sanpham sanPham) { Session session =
	 * sessionFactory.openSession(); Transaction tran = session.getTransaction();
	 * try { tran.begin(); session.delete(sanPham); tran.commit(); return true; }
	 * catch (Exception e) { tran.rollback(); } finally { session.close(); } return
	 * false; }
	 */

	/**
	 * 05/12/2020 lấy ra mã sản phẩm cuối cùng trong bảng
	 * 
	 * @return
	 */
	public String getMaxMaSanPham() {
		String s = "SELECT max(masanpham) as maxmasanpham from Sanpham";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			try {
				tran.begin();
			} catch (Exception e) {
			}
			List<?> result = session.createNativeQuery(s).getResultList();
			try {
				return result.get(0).toString();
			} catch (Exception e) {
			}
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * 05/12/2020 lấy ra mã SP tự động tăng tiếp theo
	 */
	public String getNextMaSP() {
		String maxMaSP = getMaxMaSanPham();
		if (maxMaSP == null) {
			maxMaSP = "00000000";
		} else {
			maxMaSP = maxMaSP.substring(2, maxMaSP.length());
		}
		int identity = 0;
		try {
			identity = Integer.parseInt(maxMaSP);
		} catch (Exception e) {
		}
		String StrIdentity = identity + 1 + "";
		String s = "SP00000000";
		String newMaSP = s.substring(0, s.length() - StrIdentity.length()) + StrIdentity;
		return newMaSP;
	}

}
