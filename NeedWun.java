import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NeedWun {

	private String n,m;
	private int maxscore;
	private int score;
	private int maxi = 0;
	private int maxj = 0;
	private int gap = -4;
	private int comparisons;
	public String[][] traceback;
	public int [][] s; //scoring matrix
	public NeedWun(String a, String b){
		n=a;m=b;
		s=new int[n.length()+1][m.length()+1];//comparison matrix
		traceback = new String[n.length()+1][m.length()+1];
		
		
		initalize();
		
	}
	
	public void initalize(){
		comparisons = 0;
		maxscore = 0;
		score = 0;
		s[0][0]=0;//sets the starting value of the matrix
		for(int i=1;i<=n.length();i++)
			s[i][0]=i*gap;
		for(int j=1;j<m.length()+1;j++)
			s[0][j]=j*gap;
	}
	
	private int cost(char a, char b){
		if(a==b){
			return 5;
			}//1 bonus point if the chars are the same
		else{ 
			return -3;}
	}
	
	public void drawmatrix(){
		
		for (int i = 0; i < n.length(); i++) {
		    for (int j = 0; j < m.length(); j++) {
		        System.out.print(s[i][j] + " ");
		    }
		    System.out.print("\n");
		}
	};

	public double findmax(double[] a){
		double tempmax = -10000;
		for(int k = 0; k < a.length;k++){
			if(tempmax < a[k]){
				tempmax = a[k];
			};
		}
		return tempmax;
		};
	
	public int getbackpointer(double[] a){
			double tempmax = -10000;
			int idmax = 0;
			int k = 0;
			for(k = 0; k < a.length;k++){
				if(tempmax <= a[k]){
					comparisons++;
					tempmax = a[k];
					idmax = k;
				};
			}
			return idmax;
			};
	
	public int fillmatrix(){
		for (int i=0; i<=n.length(); i++){//Needleman-Wunsch algo
			
			//System.out.print(n.charAt(i-1)+" ");
			for(int j=0; j<=m.length();j++){
				if(i>0&&j>0){
					
					double[] temp = new double[3];
					//temp[0] = 0; //SET MAX TO ZERO
					double maximum = Math.max( s[i-1][j-1]+cost(n.charAt(i-1),m.charAt(j-1)), Math.max(s[i][j-1] + gap, s[i-1][j]+  gap));
					comparisons = comparisons + 2;
					temp[0] = s[i-1][j-1]+cost(n.charAt(i-1),m.charAt(j-1)); //MAX FROM MATCH/MISMTACH
					temp[1] = s[i][j-1] + gap; //MAX FROM HORIZONTAL SHIFT
					temp[2] = s[i-1][j]+  gap;//MAX FROM VERTICAL SHIFT
					int max = getbackpointer(temp);
					switch(max){
					
					case 0:{
						traceback[i][j] = "Diag";
						break;
					}
					case 1:{
						traceback[i][j] = "Horz";
						break;
					}
					case 2:{
						traceback[i][j] = "Vert";
						break;
					}
					}
					//s[i][j]= (int) findmax(temp);
					s[i][j]= (int) maximum;
					
					
					
				}
				
				//System.out.print("  "+s[i][j] +"|"+traceback[i][j]+"|     ");
			}
			//System.out.println("   ");
			//System.out.println("   ");
		}
		return s[n.length()][m.length()];
	}
		
	public void align(){
		int j=m.length(); //columns
		int i=n.length(); //rows
		String temp = "";
		System.out.println("The index of the max score is ("+i+", "+j+")");
		while(i > 0 && j > 0){
		
		temp = traceback[i][j];
		switch(temp){
		
		case "Diag":{
			
			//System.out.println(m.charAt(j-1));
			//links[j] = "|";
			//System.out.println("| ");
			//System.out.println(n.charAt(i-1));
			score = score + cost(n.charAt(i-1),m.charAt(j-1));
			i--;
			j--;
			break;
		}
		case "Horz": {
			//System.out.println(m.charAt(j-1));
			//System.out.println(" ");
			//System.out.println("--");
			//links[j] = " ";
			score = score - 4;
			j--;
			break;
		}
		case "Vert":{
			
			//System.out.println("--");
			//System.out.println(" ");
			//System.out.println(n.charAt(j-1));
			score = score - 4;
			i--;
			break;
		}};
		
		//System.out.println("------------");
		}
	};

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String bfn ="";
		String afn ="";
		if (args.length>0) bfn=args[0];
		else 
		{
		   System.out.print("Enter the name of the FastaFiles for Sequence A:");
		   afn = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		   System.out.print("Enter the name of the FastaFiles for Sequence B:");
		   bfn = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		}
		SmithWaterman b= new SmithWaterman(bfn);
		SmithWaterman a = new SmithWaterman(afn);		
		
		String bstring=b.getSequence();//example DNA-sequence
		String astring=a.getSequence();
		int counter =0;
		//System.out.println("String A is: " + astring);
		//System.out.println("String B is: " + bstring);
		System.out.println("length of sequence a is: "+astring.length());
		System.out.println("length of sequence b is: "+bstring.length());
		
		
		NeedWun sw = new NeedWun(astring,bstring);
		long startTime = System.nanoTime();
		sw.fillmatrix();
		long difference = System.nanoTime() - startTime; 
		System.out.println("Total number of comparisons made: " + sw.comparisons);
        System.out.println("Total execution time for Needleman Wunsch: " +  String.format("%d milliseconds", TimeUnit.NANOSECONDS.toMillis(difference)  ));
        sw.align();
        System.out.println("The max score is "+sw.score);
		/*System.out.print("                     ");
		for(int i = 1; i< sw.m.length(); i++ ){
			System.out.print(sw.m.charAt(i-1)+"               ");}
		System.out.println();*/
		
		
	}
}
