package session_factory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

public class MySessionFactory {

	private static MySessionFactory instance = null;
	private SessionFactory sessionFactory;
	private FullTextSession fullTextSession;

	private MySessionFactory() {
//		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("resources/hibernate.cfg.xml")
//				.build();
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();

		Metadata meta = new MetadataSources(registry).addAnnotatedClass(entity.Chitiethoadon.class)
				.addAnnotatedClass(entity.Hoadon.class).addAnnotatedClass(entity.Sanpham.class)
				.addAnnotatedClass(entity.Taikhoan.class).addAnnotatedClass(entity.Loaisanpham.class)
				.addAnnotatedClass(entity.Nhacungcap.class).addAnnotatedClass(entity.Nhanvien.class)
				.addAnnotatedClass(entity.Khachhang.class).getMetadataBuilder().build();

		sessionFactory = meta.getSessionFactoryBuilder().build();
		Session session = sessionFactory.openSession();
		fullTextSession = Search.getFullTextSession(session);
		try {
			fullTextSession.createIndexer().startAndWait();
		} catch (InterruptedException e) {
		}
	}

	public static synchronized MySessionFactory getInstance() {
		if (instance == null)
			instance = new MySessionFactory();
		return instance;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public FullTextSession getFullTextSession() {
		return fullTextSession;
	}

}
