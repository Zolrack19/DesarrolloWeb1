package com.example;

import org.hibernate.Session;

public class Main {

	public static void main(String[] args) {
		try {
			Session s = HibernateUtil.getSession().openSession();
			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}