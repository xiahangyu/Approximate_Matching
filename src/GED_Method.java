
public class GED_Method {

	private static int m=-1;	//price for match operation
	private static int d=1;	//price for delete operation
	private static int i=1;	//price for insert operation
	private static int r=1;	//price for replace operation
	public static int getDistance(String Resource, String Target, int type){
		if(type==1)
			initial_replace_value_table();
		
		int j,k;
		int[][] distance=new int[Resource.length()+1][Target.length()+1];
		distance[0][0]=0;
		for(j=1;j<=Resource.length();j++)
			distance[j][0]=d*j;
		for(k=1;k<=Target.length();k++)
			distance[0][k]=i*k;
		
		for(j=1;j<=Resource.length();j++){
			for(k=1;k<=Target.length();k++){
				distance[j][k]=min3(
						distance[j-1][k]+d,	//Deletion
						distance[j][k-1]+Insertion(Target.charAt(k-1), type),	//Insertion
						distance[j-1][k-1]+ Equal(Resource.charAt(j-1), Target.charAt(k-1), type) //Replace or match
						);
			}
		}
		return distance[j-1][k-1];	//The distance between Resource string and Target string.
	}

	public static int Insertion(char ch, int type){
		if(type==0)
			return i;
		
		char [] vowels={'a','e','i','o','u','w','y'};
		for(int i=0;i<vowels.length;i++){
			if(ch==vowels[i])
				return i/2;
		}
		return i;
	}
	
	private static int[][] replace_value_table=new int[26][26];	//The prices of replacement for each pair of letters. In this program, the prices is based on the sounds of letterss.
	public static void initial_replace_value_table(){
		for(int i=0;i<26;i++){
			for(int j=0;j<26;j++){
				if(i==j)
					replace_value_table[i][j]=m;
				else
					replace_value_table[i][j]=r;
			}
		}
		
		char[][] group={
				{'a','e','i','o','u','w','y'},
				{'v','o'},
				{'p','f'},
				{'c','k','q','x'},
				{'j','g'},
				{'j','z'},
				{'d','t'},
				{'m','n'},
		};
		for(int i=0; i<group.length; i++){
			for(int j=0; j<group[i].length; j++){
				for(int k=0; k<group[i].length; k++){
					int a,b;
					a=group[i][j]-'a';
					b=group[i][k]-'a';
					if(j==k){
						replace_value_table[a][b]=m;
					}
					else{
						replace_value_table[a][b]=r/2;
					}
				}
			}
		}
	}
	
	public static int min3(int a,int b,int c){
		if(a<b){
			return a<c?a:c;
		}
		else{
			return b<c?b:c;
		}
	}
	
	public static int Equal(char a, char b, int type){
		if(type==0){//Type 0 has a constant price for replacement 
			return (a==b)?m:r;
		}
		else if(type==1){//Type 1 has different replacement prices which depend on a and b.This is showed in replace_value_table
			if(a!='\'' && b!='\''){
				return replace_value_table[a-'a'][b-'a'];
			}
			else if( (a=='\'' && b=='a') || (b=='\'' && a=='a') ){
				return m;
			}
			else
				return r;
		}
		
		System.out.println("Wrong type of Global_Edit_Distance!");
		System.exit(-1);
		return r;
	}
}
