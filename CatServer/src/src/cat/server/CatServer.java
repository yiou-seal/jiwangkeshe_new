package cat.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

import cat.function.CatBean;
import cat.function.CatClientBean;
import cat.util.CatUtil;

public class CatServer {
	DefaultListModel myListmodel =new DefaultListModel<>();
	private static ServerView serverView =null ;
	private static ServerSocket ss;
	public static HashMap<String, CatClientBean> onlines;//����������Ϣ
	static DefaultTableModel defaultTableModel = new DefaultTableModel();

	static Vector<String> column =new Vector<>();

	static {
		try {

			ss = new ServerSocket(8520);

			onlines = new HashMap<String, CatClientBean>();

			defaultTableModel.addColumn("�û���");
			defaultTableModel.addColumn("����ip��ַ");
			defaultTableModel.addColumn("��½ʱ��");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ClientThread extends Thread {//�������ӵģ�ÿ���û�һ���߳�

		private Socket client;
		private CatBean bean;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		public ClientThread(Socket client) {
			this.client = client;
		}

		@Override
		public void run()
		{
			try
			{
				// ��ͣ�Ĵӿͻ��˽�����Ϣ
				while (true)
				{
					// ��ȡ�ӿͻ��˽��յ���catbean��Ϣ
					ois = new ObjectInputStream(client.getInputStream());
					bean = (CatBean) ois.readObject();

					// ����catbean�У�type������һ������
					switch (bean.getType())
					{
						// �����߸���
						case 0:
						{ // ����
							// ��¼���߿ͻ����û����Ͷ˿���clientbean��
							CatClientBean cbean = new CatClientBean();
							cbean.setName(bean.getName());
							cbean.setSocket(client);
							// ��������û�
							onlines.put(bean.getName(), cbean);

							new Thread()
							{
								public void run()
								{
									myListmodel.addElement(bean.getName());
									Object[] data = new Object[3];

									data[0] = (bean.getName());
									data[1] = (onlines.get(bean.getName()).getSocket().getLocalSocketAddress().toString());
									data[2] = (CatUtil.getTimer());

									defaultTableModel.addRow(data);//��¼���û�����ip,ʱ�䣬�ӵ�����ı���
									serverView.list.setModel(myListmodel);
									serverView.table.setModel(defaultTableModel);
								}

								;
							}.start();

							// ������������catbean�������͸��ͻ���
							CatBean serverBean = new CatBean();
							serverBean.setType(0);
							serverBean.setInfo(bean.getTimer() + "  "
									+ bean.getName() + "������");
							// ֪ͨ���пͻ���������
							HashSet<String> set = new HashSet<String>();
							// �ͻ��ǳ�
							set.addAll(onlines.keySet());
							serverBean.setClients(set);
							sendAll(serverBean);
							break;
						}
						case -1:
						{ // ����
							// ������������catbean�������͸��ͻ���
							CatBean serverBean = new CatBean();
							serverBean.setType(-1);

							try
							{
								oos = new ObjectOutputStream(
										client.getOutputStream());
								oos.writeObject(serverBean);
								oos.flush();
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							onlines.remove(bean.getName());
							new Thread()
							{
								public void run()
								{
									for (int i = 0; i < defaultTableModel.getRowCount(); i++)
									{
										if (defaultTableModel.getValueAt(i, 0).equals(bean.getName()))
										{
											defaultTableModel.removeRow(i);
										}
									}
									myListmodel.removeElement(bean.getName());
									serverView.list.setModel(myListmodel);

								}

								;
							}.start();
							// ��ʣ�µ������û����������뿪��֪ͨ
							CatBean serverBean2 = new CatBean();
							serverBean2.setInfo("\r\n" + bean.getTimer() + "  "
									+ bean.getName() + "" + " ������");
							serverBean2.setType(0);
							HashSet<String> set = new HashSet<String>();
							set.addAll(onlines.keySet());
							serverBean2.setClients(set);

							sendAll(serverBean2);
							return;
						}
						case 1:
						{ // ����

							//						 ������������catbean�������͸��ͻ���
							CatBean serverBean = new CatBean();
							serverBean.setType(1);
							serverBean.setClients(bean.getClients());
							serverBean.setInfo(bean.getInfo());
							serverBean.setName(bean.getName());
							if (bean.getAttributeSet() != null)
							{
								serverBean.setAttributeSet(bean.getAttributeSet());
							}
							serverBean.setTimer(bean.getTimer());
							// ��ѡ�еĿͻ���������
							sendMessage(serverBean);
							break;
						}
						case 2:
						{ // ��������ļ�
							// ������������catbean�������͸��ͻ���
							CatBean serverBean = new CatBean();
							String info = bean.getTimer() + "  " + bean.getName()
									+ "���㴫���ļ�,�Ƿ���Ҫ����";

							serverBean.setType(2);
							serverBean.setClients(bean.getClients()); // ���Ƿ��͵�Ŀ�ĵ�
							serverBean.setFileName(bean.getFileName()); // �ļ�����
							serverBean.setSize(bean.getSize()); // �ļ���С
							serverBean.setInfo(info);
							serverBean.setName(bean.getName()); // ��Դ
							serverBean.setTimer(bean.getTimer());
							// ��ѡ�еĿͻ���������
							sendMessage(serverBean);

							break;
						}
						case 3:
						{ // ȷ�������ļ�
							CatBean serverBean = new CatBean();

							serverBean.setType(3);
							serverBean.setClients(bean.getClients()); // �ļ���Դ
							serverBean.setTo(bean.getTo()); // �ļ�Ŀ�ĵ�
							serverBean.setFileName(bean.getFileName()); // �ļ�����
							serverBean.setIp(bean.getIp());
							serverBean.setPort(bean.getPort());
							serverBean.setName(bean.getName()); // ���յĿͻ�����
							serverBean.setTimer(bean.getTimer());
							// ֪ͨ�ļ���Դ�Ŀͻ����Է�ȷ�������ļ�
							sendMessage(serverBean);
							break;
						}
						case 4:
						{
							CatBean serverBean = new CatBean();
							serverBean.setType(4);
							serverBean.setClients(bean.getClients()); // �ļ���Դ
							serverBean.setTo(bean.getTo()); // �ļ�Ŀ�ĵ�
							serverBean.setFileName(bean.getFileName());
							serverBean.setInfo(bean.getInfo());
							serverBean.setName(bean.getName());// ���յĿͻ�����
							serverBean.setTimer(bean.getTimer());
							sendMessage(serverBean);
							break;
						}
						case 9:
						{
							CatBean serverBean = new CatBean();

							serverBean.setType(9);
							serverBean.setClients(bean.getClients());
							serverBean.setTo(bean.getTo());
							serverBean.setName(bean.getName());
							serverBean.setTimer(bean.getTimer());
							sendMessage(serverBean);

							break;
						}
						case 8:
						{
							CatBean serverBean = new CatBean();

							serverBean.setType(8);
							serverBean.setIcon(bean.getIcon());
							serverBean.setClients(bean.getClients());
							serverBean.setTo(bean.getTo());
							serverBean.setName(bean.getName());
							serverBean.setTimer(bean.getTimer());
							sendMessage(serverBean);

							break;
						}
						default:
						{
							break;
						}
					}
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			{
				close();
			}
		}

		// ��ѡ�е��û���������
		private void sendMessage(CatBean serverBean) {
			// ����ȡ�����е�values
			Set<String> cbs = onlines.keySet();
			Iterator<String> it = cbs.iterator();
			// ѡ�пͻ�
			HashSet<String> clients = serverBean.getClients();
			while (it.hasNext()) {
				// ���߿ͻ�
				String client = it.next();
				// ѡ�еĿͻ����������ߵģ��ͷ���serverbean
				if (clients.contains(client)) {
					Socket c = onlines.get(client).getSocket();
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(c.getOutputStream());
						oos.writeObject(serverBean);
						oos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

		// �����е��û���������
		private void sendAll(CatBean serverBean) {
			Collection<CatClientBean> clients = onlines.values();
			Iterator<CatClientBean> it = clients.iterator();
			ObjectOutputStream oos;
			while (it.hasNext()) {
				Socket c = it.next().getSocket();
				try {
					oos = new ObjectOutputStream(c.getOutputStream());
					oos.writeObject(serverBean);
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void close() {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void start() {
		try {
			while (true) {
				Socket client = ss.accept();
				new ClientThread(client).start();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		serverView = new ServerView();
		serverView.setVisible(true);
		CatServer catServer =new CatServer();
		catServer.start();
		serverView.setCatServer(catServer);
	}

}


