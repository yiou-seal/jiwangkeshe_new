package cat.server;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultListModel;
import cat.function.Clientserverinfo;

class MyListmodel extends DefaultListModel{

	private Vector<Clientserverinfo>  clientBeans =null;
	
	public MyListmodel getModle() {
		return this;
	}
	
	@Override
	public Enumeration elements() {
		// TODO Auto-generated method stub
		return super.elements();
	}

	public MyListmodel(HashMap<String, Clientserverinfo> onlines) {
		clientBeans =new Vector<>();
		clientBeans = (Vector<Clientserverinfo>)onlines.values();
	}

	@Override
	public void addElement(Object element) {
		clientBeans.add((Clientserverinfo) element);
	}

	@Override
	public boolean removeElement(Object obj) {
		return 	clientBeans.remove(obj);
	}

	@Override
	public void clear() {
		clientBeans.removeAllElements();
	}

}
