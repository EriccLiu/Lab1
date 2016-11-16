import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Calculator {
	
	private node root;
	private Operator savedPoly ;
	private HashMap<String,Handler> handlers = new HashMap<String,Handler>();
	private ArrayList<String> variable = new ArrayList<String>();
	private boolean illegal;
	
	//���캯��
	public Calculator()
	{
		handlers.put("!simplify", new HandlerSimplify(this));
		handlers.put("!d/d", new HandlerDerivation(this));
		handlers.put("!exit",new HandlerExit(this));
		root = null;
		savedPoly = new Operator('+');
	}

	//������
	public node copy(node root)
	{
		node current = null;
		if(root!=null)
		{
			if(root instanceof Operator)
				current = new Operator(((Operator) root).getContent());
			else if(root instanceof Digit)
				current = new Digit(((Digit) root).getContent());
			else if(root instanceof Character)
				current = new Character(((Character) root).getContent());
				
			current.left = copy(root.left);
			current.right = copy(root.right);
		}
		return current;
	}
	
	//��ʾ��Ϣ
	public void showPrompt()
	{
		System.out.println();
		System.out.println("������ָ����µı��ʽ��");
		System.out.println("ָ����: !simplify !d/d !exit");
		System.out.println();
	}
	
	//����������ʽ��
	public StringBuffer midTravel(node n)
	{
		StringBuffer sb = new StringBuffer();
		if(n!=null)
		{
			if((n.left==null&&n.right!=null)||(n.left!=null&&n.right==null)){
				illegal = true ;
			}
			
			//�жϵ�ǰ���ȼ����������ȼ�������
			sb.append(midTravel(n.left));
			sb.append(n.get());
			sb.append(midTravel(n.right));
			
		}
		return sb;
		
	}
	
	//����������ʽ��
	public StringBuffer PreTravel(node n)
	{
		StringBuffer sb = new StringBuffer();
		if(n!=null)
		{
			if((n.left==null&&n.right!=null)||(n.left!=null&&n.right==null)){
				illegal = true ;
			}
			
			//�жϵ�ǰ���ȼ����������ȼ�������
			sb.append(n.get());
			sb.append(PreTravel(n.left));
			sb.append(PreTravel(n.right));
			
		}
		return sb;
		
	}
	
	//��ӡ���ṹ����ʽ
	public void print(Operator n)
	{
		if(n.getContent()=='+')
		{
			int count = 0;
			if(n.son.size()==0)
				System.out.print("0");
			else
			{
				for( node k : n.son )
				{
					if(count!=0)
						System.out.print("+");
					print((Operator)k);
					count ++;
				}
			}
		}
		else if(n.getContent()=='*')
		{
			int count = 0,factor = ((Digit)(n.son.get(0))).getContent();
			for(node k : n.son)
			{
				if(k instanceof Digit)
				{
					if(factor==1)
						continue ;
					else if(factor==-1)
						System.out.print("-");
					else{
						System.out.print(factor);
					}
				}
				else if(k instanceof Character)
				{
					if(((Character) k).getIndex()>0)
					{
						//�ж��Ƿ��ӡ�˺�
						if(count!=0)
							System.out.print("*");
						else if(factor!=1&&factor!=-1)
							System.out.print("*");
							
						System.out.print(((Character) k).getContent());
						if(((Character) k).getIndex()>1)
							System.out.print("^"+((Character) k).getIndex());
						count ++;
					}
				}
			}
		}
	}
	
	//ȥ�������������ʽ������
	public String debracket(String s)
	{
		int bracketCount = 1;
		if(s.substring(0, 1).equals("("))
			for(int i = 1 ; i < s.length() ; i++ )
			{
				if(s.substring(i, i+1).equals("("))
					bracketCount ++;
				else if(s.substring(i, i+1).equals(")"))
					bracketCount --;
				
				if(bracketCount==0)
				{
					if(i==s.length()-1)
						s = s.substring(1, s.length()-1);
					else
						break;
				}
			}
		return s;
	}

	//�ݹ鹹����ʽ��
	private node ContributeTree(char[] line) 
	{
		
		node current = null;
		int bracketCount = 0 , numCount = 0 , chCount = 0;
		int splitPos = 0;
		String temp = new String(line) ;
		
		//�ҵ����������һ���Ӻ�
		for(int i = 0 ; i < line.length ; i++ )
		{
			if(line[i]==' ')
				continue ;
			if(line[i]=='(')
				bracketCount ++;
			else if(line[i]==')')
				bracketCount --;
			else if(line[i]=='+')
			{
				if(bracketCount==0)
				{
					splitPos = i ;
					break ;
				}
			}
			else if(line[i]>='0'&&line[i]<='9')
			{
				numCount ++ ;
			}
			else if(line[i]>='a'&&line[i]<='z')
			{
				chCount ++ ;
			}
		}
		//��������ûӴ�Ӻţ����ҵ����������һ���˺�
		if(splitPos==0&&line[0]!='+')
		{
			for(int i = 0 ; i < line.length ; i++ )
			{
				if(line[i]==' ')
					continue ;
				if(line[i]=='(')
					bracketCount ++;
				else if(line[i]==')')
					bracketCount --;
				else if(line[i]=='*')
				{
					if(bracketCount==0)
					{
						splitPos = i ;
						break ;
					}
				}
				else
					continue ;
			}
		}
		//����ô��Ǳ�����
		if(line.length>0&&chCount==line.length)
		{
			current = new Character(line);
		}
		//����ô������ִ�
		if(line.length>0&&numCount==line.length)
		{
			int data = 0 ;
			for(int i = 0 ; i < line.length ; i++ )
			{
				data *= 10 ;
				data += line[i]-'0' ;
			}
			current = new Digit(data);
		}

		if(current instanceof Data)
		{
			current.left = null ;
			current.right = null ;
		}
		else if(line.length!=0)
		{		
			String left = temp.substring(0, splitPos);
			String right = temp.substring(splitPos+1, line.length);
			current = new Operator(line[splitPos]);
			
			if(left.equals(""))
				current.left = null;
			else
				current.left = ContributeTree(debracket(left).toCharArray());
			
			if(right.equals(""))
				current.right = null;
			else
				current.right = ContributeTree(debracket(right).toCharArray());
			
			if((current.left==null&&current.right!=null)||(current.left!=null&&current.right==null)){
				illegal = true;
			}
		}
		
		return current;
	}
	
	//Ԥ������ʽ�ַ�����
	public char[] preprocess(char[] line)
	{
		ArrayList process = new ArrayList();
		if(line[0]!=' ')
			process.add(line[0]);
		//��������-����������-����֮��ĳ˺�
		for( int i=1 ; i<line.length-1 ; i++ )
		{
			if(line[i]==' ')
			{
				if((line[i-1]>='0'&&line[i-1]<='9')&&(line[i+1]>='a'&&line[i+1]<='z'))
				{
					line[i]='*';
				}
				else if((line[i-1]>='a'&&line[i-1]<='z')&&(line[i+1]>='0'&&line[i+1]<='9'))
				{
					line[i]='*';
				}
				else if((line[i-1]>='a'&&line[i-1]<='z')&&(line[i+1]>='a'&&line[i+1]<='z'))
				{
					line[i]='*';
				}
				else
					continue;
			}
			else if((line[i]>='a'&&line[i]<='z')&&(line[i-1]>='0'&&line[i-1]<='9'))
			{
				process.add('*');
			}
			process.add(line[i]);
		}
		if(line[line.length-1]!=' ')
		{
			if((line[line.length-1]>='a'&&line[line.length-1]<='z')
					&&(line[line.length-2]>='0'&&line[line.length-2]<='9')){
				process.add('*');
			}
			process.add(line[line.length-1]);
		}
		
		char[] changed = new char[process.size()];
		for( int i=0 ; i<process.size() ; i++ )
		{
			changed[i] = (char)process.get(i);
		}
		return changed;
	}
	
	//��������ʽ�����������
	public void toTree( char[] line )
	{
		
		//Ԥ������ʽ
		line = preprocess(line);
			
		root = null ;
		int bracketCount = 0 ;
		
		//������������Ƿ�ƥ�䡢�Ƿ��зǷ��ַ�
		for(int i = 0 ; i < line.length ; i++ )
		{
			if(line[i]=='(')
				bracketCount ++ ;
			else if(line[i]==')')
			{
				bracketCount -- ;
				if(bracketCount<0)
				{
					System.out.println("��ʽ����");
					illegal = true ;
					break ;
				}
			}
			else if(line[i]>='0'&&line[i]<='9'||line[i]>='a'&&line[i]<='z'||line[i]=='+'||line[i]=='*')
				continue ;
			else
			{
				System.out.println("�Ƿ��ַ���"+line[i]);
				illegal = true ;
			}
		}
		//����������ƥ��
		if(bracketCount!=0)
		{
			System.out.println("���Ų�ƥ�䣡");
			illegal = true ;
		}
		
		if(!illegal){
			root = ContributeTree(line);
		}

	}	
	
	//�����б�������variable��
	private void getVar(node n)
	{
		if(n instanceof Operator)
		{
			getVar(n.left);
			getVar(n.right);
		}
		else if(n instanceof Character)
		{
			for( Object k : variable )
			{
				if(((Character) n).getContent().equals( (String)k ))
				{
					return ;
				}
			}
			variable.add( ((Character) n).getContent() );
		}
	}
	
	//չ������ʽ�ݹ麯��
	private void unfold(node n) 
	{
		if(n instanceof Operator)
		{	
			unfold(n.getLeft());
			unfold(n.getRight());
			
			if(((Operator)n).getContent()=='*')
			{	
				boolean leftIsPlus = false , rightIsPlus = false;
				
				if(n.getLeft() instanceof Operator&&((Operator)(n.getLeft())).getContent()=='+')
					leftIsPlus = true ;
				if(n.getRight() instanceof Operator&&((Operator)n.getRight()).getContent()=='+')
					rightIsPlus = true ;
				if(leftIsPlus&&rightIsPlus)
				{
					node ll = new Operator('*');
					node lr = new Operator('*');
					node rl = new Operator('*');
					node rr = new Operator('*');
					((Operator) n).set('+');
					ll.manageLeft(n.getLeft().getLeft());
					ll.manageRight(n.getRight().getLeft());
					lr.manageLeft(copy(n.getLeft().getLeft()));
					lr.manageRight(n.getRight().getRight());
					rl.manageLeft(n.getLeft().getRight());
					rl.manageRight(copy(n.getRight().getLeft()));
					rr.manageLeft(copy(n.getLeft().getRight()));
					rr.manageRight(copy(n.getRight().getRight()));
					n.left.manageLeft(ll);
					n.left.manageRight(lr);
					n.right.manageLeft(rl);
					n.right.manageRight(rr);
				}
				else if(leftIsPlus&&!rightIsPlus)
				{
					node right = new Operator('*');
					right.manageLeft(n.getLeft().getRight());
					right.manageRight(n.getRight());
					((Operator) n).set('+');
					((Operator) n.getLeft()).set('*');
					n.getLeft().manageRight(copy(n.right));
					n.manageRight(right);
				}
				else if(!leftIsPlus&&rightIsPlus)
				{
					node left = new Operator('*');
					left.manageRight(n.getRight().getLeft());
					left.manageLeft(n.getLeft());
					((Operator) n).set('+');
					((Operator) n.getRight()).set('*');
					n.getRight().manageLeft(copy(n.left));
					n.manageLeft(left);
				}				
			}
			
			unfold(n.getLeft());
			unfold(n.getRight());
	}
		
		}
	
	//��Ҷ�ӽ������÷��뵥��ʽ��target��
	private void travelLeaf(node n,Operator target)
	{
		//����������������Ҷ�ӽ�������±���
		if(n.left!=null&&n.right!=null)
		{
			travelLeaf(n.left,target);
			travelLeaf(n.right,target);
		}
		//�����Ҷ�ӽ��
		else if(n.left==null&&n.right==null)
		{
			//�����򽫵�ǰϵ�����Ͻ�������ٴ�����
			if(n instanceof Digit)
			{
				int result ;
				result = ((Digit)(target.son.get(0))).getContent()*((Digit) n).getContent();
				((Digit)(target.son.get(0))).set( result );
			}
			//��Ϊ��ĸ���ҵ��ҵ������ĸ����ָ����һ
			else if(n instanceof Character)
			{	
				for(node k : ((Operator)target).son )
				{
					if(k instanceof Character)
					{
						if(((Character) k).getContent().equals(((Character)n).getContent()))
						{
							((Character) k).setIndex(((Character) k).getIndex()+1);
						}
					}
					else if(k instanceof Digit)
						continue;
					
				}
			}
		}	
	}
	
	//�����������ʽת��Ϊ�����ʽ
	private node settle(node n)
	{
		//��ʼ���˺Ž��
		Operator sub = new Operator('*');
		sub.addSon(new Digit(1));
		
		//��ʼ���˺Ž���ӽڵ�
		for( Object k : variable )
		{
			node leaf = new Character((String)k);
			((Character)leaf).setIndex(0);
			sub.addSon(leaf);
		}
		
		//����˺Ž��
		travelLeaf(n,sub);
		
		return sub;
	}
	
	//������ʽ�ϲ����Ҵ���savedPoly��
	private void clearUp(node n)
	{	
		if(n instanceof Operator&&((Operator) n).getContent()=='+')
		{
			clearUp(n.left);
			clearUp(n.right);
		}
		else if(n instanceof Data||( n instanceof Operator&&((Operator) n).getContent()=='*'))
		{
			node subnode = settle(n);
			((Operator)savedPoly).addSon(subnode);
		}
	}

	//�Ƚ���������ʽ�Ƿ�Ϊͬ����
	private boolean compareMono(Operator a,Operator b)
	{
		boolean isSame = true;
		for(int i = 1 ; i < a.son.size() ; i++ )
		{
			if(((Character)(a.son.get(i))).getIndex()==((Character)(b.son.get(i))).getIndex())
				continue;
			else
			{
				isSame = false;
				break;
			}
		}
		return isSame ;
	}
	
	//�ϲ�ͬ����
	private void combine()
	{
		//�Ƚ���������ʽ�Ƿ�Ϊͬ����
		for(int i = 0 ; i < savedPoly.son.size() ; i++ )
		{
			for(int j = i+1 ; j < savedPoly.son.size() ; j++ )
			{
				if(compareMono((Operator)savedPoly.son.get(i),(Operator)savedPoly.son.get(j)))
				{
					int factor = ((Digit)((Operator)savedPoly.son.get(i)).son.get(0)).getContent();
					factor += ((Digit)((Operator)savedPoly.son.get(j)).son.get(0)).getContent();
					((Digit)((Operator)savedPoly.son.get(i)).son.get(0)).set(factor);;
					savedPoly.son.remove(j);
					j--;
				}
			}
		}
	}
	
	//�����ʽ�����������
	private void Save(char[] input) 
	{
		
		//��ʼ��������
		root = null;
		savedPoly = new Operator('+');
		variable.clear();
		
		//�����ʽ��������
		toTree(input);

		if(!illegal)
		{
			//�洢���б���
			getVar(root);
			
			//�����������ʽȫ��չ����ȥ���ţ�
			unfold(root);
		
			//�������ʽ
			clearUp(root);
						
			//�ϲ�ͬ����
			combine();
			
			print(savedPoly);
		}
	}
	//

	
	//������ʽָ��
	public void simplify(String cmd)
	{
		ArrayList<String> input = new ArrayList<String>();
		HashMap<Integer,Integer> pair = new HashMap<Integer,Integer>();
		String[] cut = cmd.split(" ");
		boolean exist = false;
		
		//�������ַ����еı��������Ӧֵ�浽��ϣ����
		for(int i=0 ; i < cut.length ; i++ )
		{
			exist = false;
			if(!cut[i].equals(""))
			{
				String[] divide = cut[i].split("="); 
				if(divide.length!=2)
				{
					illegal = true;
					return;
				}
				else
				{
					int value = 0;
					char[] data = divide[1].toCharArray();
					for(int j = 0 ; j < data.length ; j++)
					{
						if(data[j]>='0'&&data[j]<='9')
						{
							value *= 10;
							value += data[j]-'0';
						}
						else
						{
							illegal = true;
							return;
						}
					}
					
					//�жϸñ����Ƿ����
					for(String k : variable)
					{
						if(k.equals(divide[0]))
						{
							exist = true;
							input.add(k);
							pair.put(variable.indexOf(k)+1, value);
							break;
						}
					}
					if(!exist)
					{
						System.out.println("����"+divide[0]+"�����ڣ�");
					}
				}
			}
		}
		
		//��ֵ����ÿ������ʽ
		for( node n : savedPoly.son )
		{
			int factor = ((Digit)(((Operator)n).son.get(0))).getContent();
			for( Integer k : pair.keySet() )
			{
				Character point = (Character)((Operator)n).son.get(k);
				for( int i = 0 ; i < point.getIndex() ; i++ )
				{
					factor *= (int)pair.get(k);
				}
				((Digit)((Operator)n).son.get(0)).set(factor);
			}
			//�������ֵΪ0�����Ƴ�
			if(factor==0)
			{
				savedPoly.son.remove(n);
			}
			//�Ƴ���ֵ�ı���
			else
			{
				for(String k : input)
				{
					for(node q : ((Operator)n).son)
					{
						if(q instanceof Character&&k.equals(((Character) q).getContent()))
						{
							((Operator)n).son.remove(q);
							break;
						}
					}
				}
			}
		}
		
		//�Ƴ��������и�ֵ�ı���
		for( String k : input )
		{
			for( String p : variable )
			{
				if(k.equals(p))
				{
					variable.remove(p);
					break;
				}
			}
		}
		
		combine();
		print(savedPoly);
		
	}
	//
	
	
	//��ָ��
	public void derivation(String cmd)
	{
		int j = 0 , pos =0;
		char[] var = cmd.toCharArray(); 
		for( int i = 0 ; i < var.length ; i++,j++ )
		{
			if(j>i&&var[i]!=' ')
			{
				illegal = true;
				break;
			}
			else if(var[i]==' ')
				continue;
			else
			{
				j = i;
				for( j = i ; j < var.length ; j++ )
				{
					if(var[j]==' ')
						break;
				}
				String cut = new String(var).substring(i, j);
				illegal = true;
				for(String k:variable)
				{
					if(!(k.equals(cut)))
						continue;
					else
					{
						pos = variable.indexOf(k);
						pos ++;
						illegal = false;
						break;
					}
				}
				break;
			}
		}
		
		//��ֵ����ÿ������ʽ
		if(!illegal)
		{
			for( int i=0 ; i < savedPoly.son.size() ; i++ )
			{
				node n = savedPoly.son.get(i);
				int factor = ((Digit)(((Operator)n).son.get(0))).getContent();
				int index = ((Character)(((Operator)n).son.get(pos))).getIndex();
				factor *= index;
				((Character)(((Operator)n).son.get(pos))).setIndex(index-1);
				((Digit)(((Operator)n).son.get(0))).set(factor);
					
				//�������ֵΪ0�����Ƴ�
				if(factor==0)
				{
					savedPoly.son.remove(n);
					i--;
				}
				
			}	
			combine();
			print(savedPoly);
		}
		illegal = false;
		
	}
	//

	
	//����������
	public void calculate()
	{
		
		Scanner s = new Scanner(System.in);
		
		while(true)
		{
			showPrompt();
			String line = s.nextLine();
			line = line.toLowerCase();
			char[] input = line.toCharArray();
			
			//�д���handler��ʽ������
			if(input[0]!='!')
			{
				Save(input);
			}
			else
			{
				Handler handler = null;
				
				if(line.length()>4&&line.subSequence(0, 4).equals("!d/d"))
					handler = handlers.get("!d/d");
				else if(line.length()>4&&line.subSequence(0, 5).equals("!exit"))
					handler = handlers.get("!exit");
				else if(line.length()>9&&line.subSequence(0, 9).equals("!simplify"))
					handler = handlers.get("!simplify");
				else
					illegal = true;
				
				if(handler!=null)
				{
					if(handler.isExit())
						break;
					else if(root!=null)
						handler.doCmd(line);
					else
						System.out.println("û�д������ʽ��");
				}
				else
				{
					System.out.println("Error,illeagal input!");
				}
			}
			
			if(illegal){
				System.out.println("���Ϸ����룡");
			}

			illegal = false ;
		}
	}

	public static void main(String[] args) {
		
		Calculator c = new Calculator();
	
		c.calculate();
		
		System.out.println("Bye");
	}

}