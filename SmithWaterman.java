import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SmithWaterman {

	private String n,m;
	private int maxscore;
	private int comparisons;
	boolean start = false;
	private int maxi = 0;
	private int maxj = 0;
	private int gap = -4;
	private ArrayList<String> malign;
	private String[] links;
	private ArrayList<String> nalign;
	public String[][] traceback;
	public int [][] s; //scoring matrix
	
	public SmithWaterman(String a, String b){
		n=a;m=b;
		s=new int[n.length()+1][m.length()+1];//comparison matrix
		traceback = new String[n.length()+1][m.length()+1];
		
		malign = new ArrayList<String>();
		links = new String[n.length() + m.length()];
		nalign = new ArrayList<String>();
		
		for(int i = 0;i <  2*m.length();i++){
			if(i < m.length())
				malign.add(""+m.charAt(i));
				else{
					malign.add("");
				}
			
		};
		for(int i = 0;i <  2*n.length();i++){
			if(i < n.length())
			nalign.add(""+n.charAt(i));
			else{
				nalign.add("");
			}
			
			
		};
		initalize();
		
	}
	
	public void initalize(){
		maxscore = 0;
		comparisons = 0;
		s[0][0]=0;//sets the starting value of the matrix
		for(int i=1;i<=n.length();i++)
			s[i][0]=0;
		for(int j=1;j<m.length();j++)
			s[0][j]=0;
	}
	
	private int cost(char a, char b){
		if(a==b){
			return 5;
			}//1 bonus point if the chars are the same
		else{ 
			return -3;}
	}
	
	public double findmax(double[] a){
		double tempmax = 0;
		for(int k = 0; k < a.length;k++){
			if(tempmax < a[k]){
				tempmax = a[k];
			};
		}
		return tempmax;
		};
	
	public int getbackpointer(double[] a){
			double tempmax = 0;
			int idmax = 0;
			int k = 0;
			for(k = 0; k < a.length;k++){
				comparisons++;
				if(tempmax < a[k]){
					
					tempmax = a[k];
					idmax = k;
				};
			}
			return idmax;
			};
	
	public int fillmatrix(){
		for (int i=0; i<=n.length(); i++){//Needleman-Wunsch algo
			for(int j=0; j<=m.length();j++){
				if(i>0&&j>0){
					double[] temp = new double[4];
					temp[0] = 0; //SET MAX TO ZERO
					temp[1] = s[i-1][j-1]+cost(n.charAt(i-1),m.charAt(j-1)); //MAX FROM MATCH/MISMTACH
					temp[2] = s[i][j-1] + gap; //MAX FROM HORIZONTAL SHIFT
					temp[3] = s[i-1][j]+gap;//MAX FROM VERTICAL SHIFT
					int max = getbackpointer(temp);
					double maximum = Math.max( s[i-1][j-1]+cost(n.charAt(i-1),m.charAt(j-1)), Math.max(s[i][j-1] + gap,Math.max(0, s[i-1][j]+gap)));
					comparisons=comparisons + 3;
					switch(max){
					case 0:{
						traceback[i][j] = "None";
						break;
					}
					case 1:{
						traceback[i][j] = "Diag";
						break;
					}
					case 2:{
						traceback[i][j] = "Horz";
						break;
					}
					case 3:{
						traceback[i][j] = "Vert";
						break;
					}
					}
					//s[i][j]= (int) findmax(temp);
					s[i][j]= (int) maximum;
					comparisons++;
					if(maxscore < s[i][j]){
						
						maxscore = s[i][j];
						maxi = i;
						maxj = j;
					};
					
					
				}
				
				//System.out.print(s[i][j] +"|"+traceback[i][j]+"|     ");
			}
			//System.out.println("   ");
			//System.out.println("   ");
		}
		return s[n.length()][m.length()];
	}
		
	public void align(){
		int i=maxi;
		int j=maxj;
		String temp = "";
		System.out.println("The index of the max score is ("+i+", "+j+")");
		while(traceback[i][j] != "None" && traceback[i][j] != null){
		
		temp = traceback[i][j];
		switch(temp){
		
		case "Diag":{
			
		//	System.out.println(m.charAt(j-1));
			//links[j] = "|";
			//System.out.println("| ");
			//System.out.println(n.charAt(i-1));
			i--;
			j--;
			break;
		}
		case "Horz": {
			//System.out.println(m.charAt(j-1));
			//System.out.println(" ");
			//System.out.println("--");
			//links[j] = " ";
			
			j--;
			break;
		}
		case "Vert":{
			
			/*System.out.println("--");
			System.out.println(" ");
			System.out.println(n.charAt(j-1));*/
			i--;
			break;
		}};
		
		//System.out.println("------------");
		}
	};

	
	
	 private String [] description;
	    private String [] sequence;

	    public SmithWaterman(String filename)
	    {
	       	readSequenceFromFile(filename);
	    }

	    void readSequenceFromFile(String file)
	    {
		ArrayList desc= new ArrayList();
		ArrayList seq = new ArrayList();
		try{
	        BufferedReader in     = new BufferedReader( new FileReader( file ) );
	        StringBuffer   buffer = new StringBuffer();
	        String         line   = in.readLine();
	     
	        if( line == null )
	            throw new IOException( file + " is an empty file" );
	     
	        if( line.charAt( 0 ) != '>' )
	            throw new IOException( "First line of " + file + " should start with '>'" );
	        else
	            desc.add(line);
	        for( line = in.readLine().trim(); line != null; line = in.readLine() )
		{
	            if( line.length()>0 && line.charAt( 0 ) == '>' )
		    {
			seq.add(buffer.toString());
			buffer = new StringBuffer();
			desc.add(line);
		    } else  
	            	buffer.append( line.trim() );
	        }   
	        if( buffer.length() != 0 )
		     seq.add(buffer.toString());
	      }catch(IOException e)
	      {
	        System.out.println("Error when reading "+file);
	        e.printStackTrace();
	      }

		description = new String[desc.size()];
		sequence = new String[seq.size()];
		for (int i=0; i< seq.size(); i++)
		{
		  description[i]=(String) desc.get(i);
		  sequence[i]=(String) seq.get(i);
		}
	 
	    }
	    
	    //return first sequence as a String
	    public String getSequence(){ return sequence[0];}

	    //return first xdescription as String
	    public String getDescription(){return description[0];}

	    //return sequence as a String
	    public String getSequence(int i){ return sequence[i];}

	    //return description as String
	    public String getDescription(int i){return description[i];}
	    
	    public int size(){return sequence.length;}
	
	
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
		long heapsize = Runtime.getRuntime().totalMemory();
        System.out.println("heapsize is :: " + heapsize);
    
		
		SmithWaterman sw = new SmithWaterman(astring,bstring);
		long startTime = System.nanoTime(); //START TIMER
		sw.fillmatrix();
		System.out.println("The max score is "+sw.maxscore);
		long difference = System.nanoTime() - startTime; 
		System.out.println("Total number of comparisons made: " + sw.comparisons);
        System.out.println("Total execution time for Smith Waterman: " +  String.format("%d milliseconds", TimeUnit.NANOSECONDS.toMillis(difference)  ));
	
		sw.align();
	
	}
}
