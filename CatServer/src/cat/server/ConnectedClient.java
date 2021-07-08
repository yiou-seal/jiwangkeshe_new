package cat.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import cat.function.CatBean;
import cat.function.Clientserverinfo;
import cat.util.CatUtil;

import database.*;
import database.entity.UsersEntity;

public class ConnectedClient
{
	public static int portNumber=8520;
	DefaultListModel myListmodel =new DefaultListModel<>();
	private static ServerView serverView =null ;
	private static ServerSocket ss;
	public static HashMap<String, Clientserverinfo> onlines;//����������Ϣ
	static DefaultTableModel defaultTableModel = new DefaultTableModel();

	static Vector<String> column =new Vector<>();

	static {
		try {

			ss = new ServerSocket(portNumber);

			onlines = new HashMap<String, Clientserverinfo>();

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
		databasesess dbsession;
		CommandPraser cp;
		HashSet<String> onlinefrind = new HashSet<String>();
		ArrayList<String> friends=new ArrayList<String>();

		public ClientThread(Socket client,databasesess dbsession) {
			this.client = client;
			this.dbsession=dbsession;
			cp=new CommandPraser(dbsession);
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
							Clientserverinfo cbean = new Clientserverinfo();
							cbean.setName(bean.getName());
							cbean.setSocket(client);
							cbean.setThreadname(Thread.currentThread().getName());
							// ���������û�
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
							// Ѱ�Һ����б�
							friends=dbsession.getfriendname(bean.getName());
							//�ҳ����ߵĺ���
							getonlinefriends();
							//onlinefrind.addAll(onlines.keySet());
							serverBean.setClients(onlinefrind);
							sendMessage(serverBean);

							//���淢�Ͱ���������Ϣ�İ�

							sendfriendsinfo();

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

							//�ҳ����ߵĺ���
							getonlinefriends();
							//onlinefrind.addAll(onlines.keySet());
							serverBean.setClients(onlinefrind);
							sendMessage(serverBean);

//							HashSet<String> set = new HashSet<String>();
//							set.addAll(onlines.keySet());
//							serverBean2.setClients(set);
//
//							sendAll(serverBean2);




							//���̽���
							return;
						}
						default:
						{
							cp.runCommand(this,bean);
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

		private void addfriend()
		{
			CatBean serverBean = new CatBean();
			serverBean.setType(12);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			//serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			String[] str=bean.getInfo().split("\\$");
			serverBean.setInfo( String.valueOf(dbsession.setnewfriend(str[0],str[1])));
			sendMessage(serverBean);
			//���º����б�
			friends=dbsession.getfriendname(bean.getName());
			//�ҳ����ߵĺ���
			getonlinefriends();
			//���淢�Ͱ���������Ϣ�İ����������ӵ���
			sendfriendsinfo();

			//�������ӵ��˷���Ϣ
			serverBean.setType(12);
			serverBean.setIcon(bean.getIcon());
			HashSet<String> target = new HashSet<String>();
			target.add(str[1]);
			serverBean.setClients(target);
			//serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());

			serverBean.setInfo(str[0]);
			sendMessage(serverBean);
		}

		private void edituserinfo()
		{
			CatBean serverBean = new CatBean();
			serverBean.setType(11);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//��Ҫ��
			dbsession.setuserinfo(new UsersEntity(bean.getInfo()));
			serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
			sendMessage(serverBean);
		}

		private void getuserinfo()
		{
			CatBean serverBean = new CatBean();

			serverBean.setType(10);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
			sendMessage(serverBean);
		}

		private void sendfriendsinfo()
		{
			CatBean serverBean = new CatBean();
			serverBean.setType(13);//����������Ϣ�İ�
			serverBean.setInfo(onlinefrind.stream().map(String::valueOf).collect(Collectors.joining("$")));
			HashSet<String> target = new HashSet<String>();
			target.add(bean.getName());
			serverBean.setName(bean.getName());
			serverBean.setClients(target);
			sendMessage(serverBean);
		}

		private void signin()
		{
			UsersEntity user= dbsession.getuserinfo(bean.getName());
			boolean result=user.getPassword().equals(bean.getInfo());

			CatBean serverBean = new CatBean();
			serverBean.setType(14);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//��Ҫ��
			serverBean.setInfo(String.valueOf(result));
			sendMessage(serverBean);
		}

		private void signup()
		{
			UsersEntity user= dbsession.getuserinfo(bean.getName());
			boolean result=user.getPassword().equals(bean.getInfo());

			CatBean serverBean = new CatBean();
			serverBean.setType(14);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//��Ҫ��
			serverBean.setInfo(String.valueOf(result));
			sendMessage(serverBean);
		}

		void getonlinefriends()
		{
			onlinefrind.clear();
			// �ҳ����ߵĺ���
			for (String key : onlines.keySet()) {
				if (friends.contains(key))
				{
					onlinefrind.add(key);
				}
			}
		}

		private void chat()
		{
			//		������������catbean�������͸��ͻ���
			CatBean serverBean = new CatBean();
			serverBean.setType(1);
			serverBean.setClients(bean.getClients());//Ŀ���û�
			serverBean.setInfo(bean.getInfo());
			serverBean.setName(bean.getName());
			if (bean.getAttributeSet() != null)
			{
				serverBean.setAttributeSet(bean.getAttributeSet());
			}
			serverBean.setTimer(bean.getTimer());
			// ��ѡ�еĿͻ���������
			sendMessage(serverBean);
		}


		// ��ѡ�е��û���������
		void sendMessage(CatBean serverBean) {
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
			Collection<Clientserverinfo> clients = onlines.values();
			Iterator<Clientserverinfo> it = clients.iterator();
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
		databasesess dbs=new databasesess();
		try {
			while (true) {
				Socket client = ss.accept();
				new ClientThread(client,dbs).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		serverView = new ServerView();
		serverView.setVisible(true);
		ConnectedClient connectedClient =new ConnectedClient();
		connectedClient.start();
		serverView.setCatServer(connectedClient);
	}

}

