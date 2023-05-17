package com.learn.mycart.helper;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryProvider {
	private static volatile SessionFactory factory;

	public static SessionFactory getFactory() {
		if (factory == null) {
			synchronized (FactoryProvider.class) {
					try {
						factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		return factory;
	}
}










//package com.learn.mycart.helper;
//
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//
//public class FactoryProvider {
//	private static SessionFactory factory;
//	public static  SessionFactory getFactory()
//	{
//		try {
//			if(factory == null)
//			{
//			factory=new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//			}
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//		return factory;
//	}
//}
